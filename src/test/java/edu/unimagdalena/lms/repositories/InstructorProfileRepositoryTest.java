package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.domine.entities.*;
import edu.unimagdalena.lms.domine.repositories.InstructorProfileRepository;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class InstructorProfileRepositoryTest extends AbstractIntegrationDBTest {

    @Autowired
    private InstructorProfileRepository instructorProfileRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @BeforeEach
    void clean() {
        instructorProfileRepository.deleteAll();
        instructorRepository.deleteAll();
    }

    private Instructor createInstructor() {
        Instructor instructor = Instructor.builder()
                .email("instructor" + UUID.randomUUID() + "@test.com")
                .fullName("Instructor Test")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return instructorRepository.save(instructor);
    }

    private InstructorProfile createInstructorProfile(Instructor instructor) {
        InstructorProfile profile = InstructorProfile.builder()
                .instructor(instructor)
                .bio("Bio Test")
                .phone("123456789")
                .build();

        return instructorProfileRepository.save(profile);
    }

    @Test
    // Para este test se crea un perfil de instructor, luego se verifica si se encuentra por instructorId
    void shouldFindByInstructorId() {
        Instructor instructor = createInstructor();
        InstructorProfile profile = createInstructorProfile(instructor);

        Optional<InstructorProfile> result = instructorProfileRepository.findByInstructorId(instructor.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(profile.getId());
    }

    @Test
    // Para este test se crea un perfil de instructor, luego se verifica si se encuentra por teléfono
    void shouldFindByPhone() {
        Instructor instructor = createInstructor();
        InstructorProfile profile = createInstructorProfile(instructor);

        Optional<InstructorProfile> result = instructorProfileRepository.findByPhone("123456789");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(profile.getId());
    }

    @Test
    // Para este test se crea un perfil de instructor, luego se verifica si existe por instructorId
    void shouldExistsByInstructorId() {
        Instructor instructor = createInstructor();
        createInstructorProfile(instructor);

        boolean exists = instructorProfileRepository.existsByInstructorId(instructor.getId());

        assertThat(exists).isTrue();
    }

    @Test
    // Para este test se crea un perfil de instructor, luego se verifica si se obtiene el perfil con instructor cargado
    void shouldFindProfileWithInstructorById() {
        Instructor instructor = createInstructor();
        InstructorProfile profile = createInstructorProfile(instructor);

        Optional<InstructorProfile> result = instructorProfileRepository.findProfileWithInstructorById(instructor.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getInstructor().getId()).isEqualTo(instructor.getId());
    }
}