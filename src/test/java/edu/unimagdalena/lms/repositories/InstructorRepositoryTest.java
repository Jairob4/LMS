package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class InstructorRepositoryTest extends AbstractIntegrationDBTest {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void clean() {
        courseRepository.deleteAll();
        instructorRepository.deleteAll();
        studentRepository.deleteAll();
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

    private Course createCourse(Instructor instructor) {
        Course course = Course.builder()
                .instructor(instructor)
                .title("Course " + UUID.randomUUID())
                .status("ACTIVE")
                .active(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return courseRepository.save(course);
    }

    @Test
    // Para este test se crea un instructor, luego se verifica si se encuentra por email
    void shouldFindByEmail() {
        Instructor instructor = createInstructor();

        Optional<Instructor> result = instructorRepository.findByEmail(instructor.getEmail());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(instructor.getId());
    }

    @Test
    // Para este test se crea un instructor, luego se verifica si existe por email
    void shouldExistsByEmail() {
        Instructor instructor = createInstructor();

        boolean exists = instructorRepository.existsByEmail(instructor.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    // Para este test se crean instructores con nombres diferentes, luego se verifica la búsqueda por nombre que contenga una cadena
    void shouldFindByFullNameContaining() {
        Instructor instructor1 = createInstructor();
        instructor1.setFullName("John Doe");
        instructorRepository.save(instructor1);

        Instructor instructor2 = createInstructor();
        instructor2.setFullName("Jane Smith");
        instructorRepository.save(instructor2);

        List<Instructor> johnInstructors = instructorRepository.findByFullNameContaining("John");

        assertThat(johnInstructors).hasSize(1);
        assertThat(johnInstructors.get(0).getFullName()).isEqualTo("John Doe");
    }

    @Test
    // Para este test se crea un instructor con cursos activos, luego se verifica si se encuentra en la lista de instructores con cursos activos
    void shouldFindInstructorsWithActiveCourses() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        course.setActive(true);
        courseRepository.save(course);

        List<Instructor> instructorsWithActiveCourses = instructorRepository.findInstructorsWithActiveCourses();

        assertThat(instructorsWithActiveCourses).hasSize(1);
        assertThat(instructorsWithActiveCourses.get(0).getId()).isEqualTo(instructor.getId());
    }

    @Test
    // Para este test se crea un instructor con cursos, luego se verifica si se obtiene el instructor con cursos cargados
    void shouldFindByIdWithCourses() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);

        Optional<Instructor> result = instructorRepository.findByIdWithCourses(instructor.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getCourses()).hasSize(1);
        assertThat(result.get().getCourses().iterator().next().getId()).isEqualTo(course.getId());
    }

    @Test
    // Para este test se crean instructores con cursos, luego se verifica el conteo de cursos por instructor
    void shouldCountCoursesByInstructor() {
        Instructor instructor1 = createInstructor();
        createCourse(instructor1);
        createCourse(instructor1);

        Instructor instructor2 = createInstructor();
        createCourse(instructor2);

        List<Object[]> result = instructorRepository.countCoursesByInstructor();

        assertThat(result).hasSize(2);
        // Verificar conteos
        for (Object[] row : result) {
            UUID id = (UUID) row[0];
            Long count = (Long) row[2];
            if (id.equals(instructor1.getId())) {
                assertThat(count).isEqualTo(2L);
            } else if (id.equals(instructor2.getId())) {
                assertThat(count).isEqualTo(1L);
            }
        }
    }
}