package edu.unimagdalena.lms.domine.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.unimagdalena.lms.domine.entities.Instructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, UUID> {

    // ORM - buscar por correo electrónico
    Optional<Instructor> findByEmail(String email);

    // ORM - verificar si existe un instructor con ese email
    boolean existsByEmail(String email);

    // JPQL - buscar instructor por nombre completo (campo con guion bajo)
    @Query("SELECT i FROM Instructor i WHERE LOWER(i.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Instructor> findByFullNameContaining(@Param("name") String name);

    // ORM - buscar instructor por nombre completo con paginacion
    Page<Instructor> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    // JPQL - obtener instructores con cursos activos
    @Query("SELECT DISTINCT i FROM Instructor i JOIN i.courses c WHERE c.active = true")
    List<Instructor> findInstructorsWithActiveCourses();

    // JPQL - obtener instructores con sus cursos cargados
    @Query("SELECT DISTINCT i FROM Instructor i LEFT JOIN FETCH i.courses WHERE i.id = :id")
    Optional<Instructor> findByIdWithCourses(@Param("id") UUID id);

    // JPQL - contar cursos por instructor
    @Query("SELECT i.id, i.fullName, COUNT(c) FROM Instructor i LEFT JOIN i.courses c GROUP BY i.id, i.fullName")
    List<Object[]> countCoursesByInstructor();
}
