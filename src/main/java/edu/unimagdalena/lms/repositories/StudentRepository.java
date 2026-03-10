package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    // ORM - buscar por correo electrónico
    Optional<Student> findByEmail(String email);

    // ORM - verificar si existe un estudiante con ese email
    boolean existsByEmail(String email);

    // JPQL - buscar estudiante por nombre completo (campo con guion bajo)
    @Query("SELECT s FROM Student s WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> findByFullNameContaining(@Param("name") String name);

    // JPQL - obtener estudiantes con sus matrículas cargadas
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.enrollments WHERE s.id = :id")
    Optional<Student> findByIdWithEnrollments(@Param("id") UUID id);

    // JPQL - obtener estudiantes con al menos una matrícula activa
    @Query("SELECT DISTINCT s FROM Student s JOIN s.enrollments e WHERE e.status = :status")
    List<Student> findStudentsWithEnrollmentStatus(@Param("status") String status);

    // JPQL - obtener estudiantes que han presentado al menos una evaluación
    @Query("SELECT DISTINCT s FROM Student s WHERE EXISTS (SELECT a FROM Assesment a WHERE a.student = s)")
    List<Student> findStudentsWithAssesments();

    // JPQL - obtener puntaje promedio de las evaluaciones de un estudiante
    @Query("SELECT AVG(a.score) FROM Assesment a WHERE a.student.id = :studentId")
    Double findAverageScoreByStudentId(@Param("studentId") UUID studentId);
}
