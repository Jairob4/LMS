package edu.unimagdalena.lms.domine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.unimagdalena.lms.domine.entities.Enrollment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    // ORM - buscar matrículas de un estudiante
    Page<Enrollment> findByStudentId(UUID studentId, Pageable pageable);

    // ORM - buscar matrículas de un curso
    Page<Enrollment> findByCourseId(UUID courseId, Pageable pageable);

    // ORM - buscar por estado
    Page<Enrollment> findByStatus(String status, Pageable pageable);

    // ORM - buscar por nombre completo del estudiante
    Page<Enrollment> findByStudentFullName(String fullName, Pageable pageable);

    // ORM - buscar por título del curso
    Page<Enrollment> findByCourseTitle(String title, Pageable pageable);

    // ORM - buscar matrículas de un estudiante por estado
    Page<Enrollment> findByStudentIdAndStatus(UUID studentId, String status, Pageable pageable);

    // ORM - buscar matrículas creadas después de una fecha
    Page<Enrollment> findByEnrolledAtAfter(Instant enrolledAt, Pageable pageable);

    // ORM - buscar matrículas en un rango de fechas
    Page<Enrollment> findByEnrolledAtBetween(Instant start, Instant end, Pageable pageable);

    // JPQL - verificar si un estudiante ya está matriculado en un curso 
    @Query("SELECT COUNT(e) > 0 FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
    boolean existsByStudentIdAndCourseId(@Param("studentId") UUID studentId,
                                          @Param("courseId") UUID courseId);

    // JPQL - contar matrículas por estado
    @Query("SELECT e.status, COUNT(e) FROM Enrollment e GROUP BY e.status")
    List<Object[]> countEnrollmentsByStatus();

    // JPQL - contar matrículas por curso
    @Query("SELECT e.course.id, e.course.title, COUNT(e) FROM Enrollment e GROUP BY e.course.id, e.course.title")
    Page<Object[]> countEnrollmentsByCourse(Pageable pageable);

    // JPQL - obtener matrículas activas con datos del estudiante
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.student WHERE e.status = :status")
    Page<Enrollment> findEnrollmentsWithStudentByStatus(@Param("status") String status, Pageable pageable);

    // JPQL - obtener el número de matrículas de un estudiante
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId")
    long countByStudentId(@Param("studentId") UUID studentId);
}
