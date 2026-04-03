package edu.unimagdalena.lms.domine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.unimagdalena.lms.domine.entities.Assessment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {

    // ORM - buscar evaluaciones de un estudiante
    Page<Assessment> findByStudentId(UUID studentId, Pageable pageable);

    // ORM - buscar evaluaciones por nombre completo del estudiante
    Page<Assessment> findByStudentFullName(String fullName, Pageable pageable);

    // ORM - buscar evaluaciones de un curso
    Page<Assessment> findByCourseId(UUID courseId, Pageable pageable);

    // ORM - buscar por tipo de evaluación
    Page<Assessment> findByType(String type, Pageable pageable);

    // ORM - buscar evaluaciones con puntaje mayor o igual a un valor
    Page<Assessment> findByScoreGreaterThanEqual(int score, Pageable pageable);

    // ORM - buscar evaluaciones por estudiante y puntaje mínimo
    Page<Assessment> findByStudentIdAndScoreGreaterThanEqual(UUID studentId, int score, Pageable pageable);

    // ORM - buscar evaluaciones por estudiante y tipo
    Page<Assessment> findByStudentIdAndType(UUID studentId, String type, Pageable pageable);

    // ORM - buscar evaluaciones entre dos fechas
    Page<Assessment> findByTakenAtBetween(Instant start, Instant end, Pageable pageable);

    // JPQL - obtener puntaje promedio de un curso
    @Query("SELECT AVG(a.score) FROM Assessment a WHERE a.course.id = :courseId")
    Double findAverageScoreByCourseId(@Param("courseId") UUID courseId);

    // JPQL - obtener evaluaciones de un estudiante en un curso específico
    @Query("SELECT a FROM Assessment a WHERE a.student.id = :studentId AND a.course.id = :courseId")
    Page<Assessment> findByStudentIdAndCourseId(@Param("studentId") UUID studentId,
                                               @Param("courseId") UUID courseId,
                                               Pageable pageable);

    // JPQL - obtener evaluaciones de un estudiante con puntaje mayor a un umbral
    @Query("SELECT a FROM Assessment a WHERE a.student.id = :studentId AND a.score >= :minScore ORDER BY a.score DESC")
    Page<Assessment> findPassingAssessmentsByStudent(@Param("studentId") UUID studentId,
                                                   @Param("minScore") int minScore,
                                                   Pageable pageable);

    // JPQL - obtener puntaje máximo por tipo
    @Query("SELECT a.type, MAX(a.score) FROM Assessment a GROUP BY a.type")
    List<Object[]> findMaxScoreByType();
}
