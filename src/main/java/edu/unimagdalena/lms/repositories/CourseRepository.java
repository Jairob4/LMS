package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    // ORM - buscar cursos de un instructor
    List<Course> findByInstructorId(UUID instructorId);

    // ORM - buscar por estado
    List<Course> findByStatus(String status);

    // ORM - buscar cursos activos
    List<Course> findByActive(boolean active);

    // ORM - buscar por título que contenga una cadena
    List<Course> findByTitleContainingIgnoreCase(String keyword);

    // ORM - buscar cursos activos de un instructor
    List<Course> findByInstructorIdAndActive(UUID instructorId, boolean active);

    // JPQL - obtener cursos con sus lecciones cargadas (fetch join)
    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.lessons WHERE c.id = :id")
    Optional<Course> findByIdWithLessons(@Param("id") UUID id);

    // JPQL - obtener cursos activos de un instructor por nombre del instructor
    @Query("SELECT c FROM Course c WHERE c.instructor.id = :instructorId AND c.active = true ORDER BY c.createdAt DESC")
    List<Course> findActiveCoursesByInstructor(@Param("instructorId") UUID instructorId);

    // JPQL - contar cursos por estado
    @Query("SELECT c.status, COUNT(c) FROM Course c GROUP BY c.status")
    List<Object[]> countCoursesByStatus();

    // JPQL - buscar cursos que tengan al menos una lección
    @Query("SELECT DISTINCT c FROM Course c WHERE SIZE(c.lessons) > 0")
    List<Course> findCoursesWithLessons();
}
