package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Assesment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssesmentRepository extends JpaRepository<Assesment, UUID> {

    // ORM - buscar evaluaciones de un estudiante
    List<Assesment> findByStudentId(UUID studentId);

    // ORM - buscar evaluaciones de un curso
    List<Assesment> findByCourseId(UUID courseId);

    // ORM - buscar por tipo de evaluación
    List<Assesment> findByType(String type);

    // ORM - buscar evaluaciones con puntaje mayor o igual a un valor
    List<Assesment> findByScoreGreaterThanEqual(int score);

    // ORM - buscar evaluaciones entre dos fechas
    List<Assesment> findByTakenAtBetween(Instant start, Instant end);

    // JPQL - obtener puntaje promedio de un curso
    @Query("SELECT AVG(a.score) FROM Assesment a WHERE a.course.id = :courseId")
    Double findAverageScoreByCourseId(@Param("courseId") UUID courseId);

    // JPQL - obtener evaluaciones de un estudiante en un curso específico
    @Query("SELECT a FROM Assesment a WHERE a.student.id = :studentId AND a.course.id = :courseId")
    List<Assesment> findByStudentIdAndCourseId(@Param("studentId") UUID studentId,
                                                @Param("courseId") UUID courseId);

    // JPQL - obtener evaluaciones de un estudiante con puntaje mayor a un umbral
    @Query("SELECT a FROM Assesment a WHERE a.student.id = :studentId AND a.score >= :minScore ORDER BY a.score DESC")
    List<Assesment> findPassingAssesmentsByStudent(@Param("studentId") UUID studentId,
                                                   @Param("minScore") int minScore);

    // JPQL - obtener puntaje máximo por tipo
    @Query("SELECT a.type, MAX(a.score) FROM Assesment a GROUP BY a.type")
    List<Object[]> findMaxScoreByType();
}
