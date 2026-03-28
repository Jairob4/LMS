package edu.unimagdalena.lms.domine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.unimagdalena.lms.domine.entities.Enrollment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    // ORM - buscar matrículas de un estudiante
    List<Enrollment> findByStudentId(UUID studentId);

    // ORM - buscar matrículas de un curso
    List<Enrollment> findByCourseId(UUID courseId);

    // ORM - buscar por estado
    List<Enrollment> findByStatus(String status);

    // ORM - buscar matrículas de un estudiante por estado
    List<Enrollment> findByStudentIdAndStatus(UUID studentId, String status);

    // ORM - buscar matrículas creadas después de una fecha
    List<Enrollment> findByEnrolledAtAfter(Instant enrolledAt);

    // JPQL - verificar si un estudiante ya está matriculado en un curso 
    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
    boolean existsByStudentIdAndCourseId(@Param("studentId") UUID studentId,
                                          @Param("courseId") UUID courseId);

    // JPQL - contar matrículas por estado
    @Query("SELECT e.status, COUNT(e) FROM Enrollment e GROUP BY e.status")
    List<Object[]> countEnrollmentsByStatus();

    // JPQL - obtener matrículas activas con datos del estudiante
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student WHERE e.status = :status")
    List<Enrollment> findEnrollmentsWithStudentByStatus(@Param("status") String status);

    // JPQL - obtener el número de matrículas de un estudiante
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId")
    long countByStudentId(@Param("studentId") UUID studentId);
}
