package edu.unimagdalena.lms.domine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.unimagdalena.lms.domine.entities.Student;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    // ORM - buscar por correo electrónico
    Optional<Student> findByEmail(String email);

    // ORM - verificar si existe un estudiante con ese email
    boolean existsByEmail(String email);

    // JPQL - buscar estudiante por nombre completo (campo con guion bajo)
    @Query("SELECT s FROM Student s WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Student> findByFullNameContaining(@Param("name") String name, Pageable pageable);

    // JPQL - obtener estudiantes con sus matrículas cargadas
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.enrollments WHERE s.id = :id")
    Optional<Student> findByIdWithEnrollments(@Param("id") UUID id);

    // JPQL - obtener estudiantes con al menos una matrícula activa
    @Query("SELECT DISTINCT s FROM Student s JOIN s.enrollments e WHERE e.status = :status")
    Page<Student> findStudentsWithEnrollmentStatus(@Param("status") String status, Pageable pageable);

    // JPQL - obtener estudiantes que han presentado al menos una evaluación
    @Query("SELECT DISTINCT s FROM Student s WHERE EXISTS (SELECT a FROM Assessment a WHERE a.student = s)")
    Page<Student> findStudentsWithAssessments(Pageable pageable);

    // JPQL - obtener puntaje promedio de las evaluaciones de un estudiante
    @Query("SELECT AVG(a.score) FROM Assessment a WHERE a.student.id = :studentId")
    Double findAverageScoreByStudentId(@Param("studentId") UUID studentId);
}
