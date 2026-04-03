package edu.unimagdalena.lms.domine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.unimagdalena.lms.domine.entities.Lesson;

import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    // ORM - obtener todas las lecciones de un curso
    Page<Lesson> findByCourseId(UUID courseId, Pageable pageable);

    // ORM - buscar lección por título exacto
    Optional<Lesson> findByTitle(String title);

    // ORM - buscar lecciones cuyo título contenga una cadena
    Page<Lesson> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    // ORM - buscar lecciones por índice de orden
    Optional<Lesson> findByCourseIdAndOrderIndex(UUID courseId, int orderIndex);

    // JPQL - obtener lecciones de un curso ordenadas por índice
    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId ORDER BY l.orderIndex ASC")
    Page<Lesson> findByCourseIdOrderByIndex(@Param("courseId") UUID courseId, Pageable pageable);

    // JPQL - contar lecciones por curso
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.course.id = :courseId")
    long countByCourseId(@Param("courseId") UUID courseId);

    // JPQL - obtener la última lección de un curso (mayor order_index)
    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId AND l.orderIndex = (SELECT MAX(l2.orderIndex) FROM Lesson l2 WHERE l2.course.id = :courseId)")
    Optional<Lesson> findLastLessonByCourseId(@Param("courseId") UUID courseId);

    // JPQL - obtener lecciones con cursos registrados
    @Query("SELECT DISTINCT l FROM Lesson l WHERE EXISTS (SELECT c FROM Course c WHERE c.id = l.course.id)")
    Page<Lesson> findLessonsWithCourse(Pageable pageable);
}
