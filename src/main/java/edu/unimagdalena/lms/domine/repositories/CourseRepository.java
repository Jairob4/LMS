package edu.unimagdalena.lms.domine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.unimagdalena.lms.domine.entities.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    // ORM - buscar cursos de un instructor
    Page<Course> findByInstructorId(UUID instructorId, Pageable pageable);

    // ORM - buscar por estado
    Page<Course> findByStatus(String status, Pageable pageable);

    // ORM - buscar cursos activos
    Page<Course> findByActive(boolean active, Pageable pageable);

    // ORM - buscar por título que contenga una cadena
    Page<Course> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    // ORM - buscar cursos activos de un instructor
    Page<Course> findByInstructorIdAndActive(UUID instructorId, boolean active, Pageable pageable);

    // JPQL - obtener cursos con sus lecciones cargadas (fetch join)
    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id")
    Optional<Course> findByIdWithLessons(@Param("id") UUID id);

    // JPQL - obtener cursos activos de un instructor por nombre del instructor
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId AND c.active = true ORDER BY c.createdAt DESC")
    Page<Course> findActiveCoursesByInstructor(@Param("instructorId") UUID instructorId, Pageable pageable);

    // JPQL - contar cursos por estado
    @Query("SELECT c.status, COUNT(c) FROM Course c GROUP BY c.status")
    List<Object[]> countCoursesByStatus();

    // JPQL - buscar cursos que tengan al menos una lección
    @Query("SELECT DISTINCT c FROM Course c WHERE SIZE(c.lessons) > 0")
    Page<Course> findCoursesWithLessons(Pageable pageable);

    // JPQL - buscar cursos que tengan al menos una matrícula activa
    @Query("SELECT DISTINCT c FROM Course c JOIN c.enrollments e WHERE e.status = 'ACTIVE'")
    Page<Course> findCoursesWithActiveEnrollments(Pageable pageable);

    // JPQL - buscar cursos que tengan al menos una evaluación con puntaje mayor a un valor
    @Query("SELECT DISTINCT c FROM Course c JOIN c.assessments a WHERE a.score > :score")
    Page<Course> findCoursesWithAssessmentsAboveScore(@Param("score") double score, Pageable pageable);
}
