package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.InstructorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, UUID> {

    // ORM - buscar perfil por instructor
    Optional<InstructorProfile> findByInstructorId(UUID instructorId);

    // ORM - buscar por teléfono
    Optional<InstructorProfile> findByPhone(String phone);

    // ORM - verificar si existe perfil para un instructor
    boolean existsByInstructorId(UUID instructorId);

    // JPQL - obtener perfil con datos completos del instructor
    @Query("SELECT ip FROM InstructorProfile ip JOIN FETCH ip.instructor WHERE ip.instructor.id = :instructorId")
    Optional<InstructorProfile> findProfileWithInstructorById(@Param("instructorId") UUID instructorId);
}
