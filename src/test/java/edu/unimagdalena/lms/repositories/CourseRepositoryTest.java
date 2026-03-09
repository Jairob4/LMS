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

public class CourseRepositoryTest extends AbstractIntegrationDBTest {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @BeforeEach
    void clean(){
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

    private Course createCourse(Instructor instructor){
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

    private Lesson createLesson(Course course) {
        Lesson lesson = Lesson.builder()
                .course(course)
                .title("Lesson " + UUID.randomUUID())
                .orderIndex(1)
                .build();

        return lessonRepository.save(lesson);
    }

    @Test
    //para este test se crean un instructor y se le asignan dos cursos, luego se verifica si se obtienen ambos cursos al buscar por el id del instructor
    void testFindByInstructorId() {
        Instructor instructor = createInstructor();
        Course course1 = createCourse(instructor);
        Course course2 = createCourse(instructor);

        List<Course> courses = courseRepository.findByInstructorId(instructor.getId());

        assertThat(courses).hasSize(2);
        assertThat(courses).extracting("id").containsExactlyInAnyOrder(course1.getId(), course2.getId());
    }

    @Test
    //para este test se crean dos cursos con estado diferente, luego se verifica si se obtienen ambos cursos al buscar por estado
    void shouldFindByStatus() {
        Instructor instructor = createInstructor();
        Course course1 = createCourse(instructor);
        Course course2 = createCourse(instructor);

        course1.setStatus("ACTIVE");
        courseRepository.save(course1);

        course2.setStatus("INACTIVE");
        courseRepository.save(course2);

        List<Course> activeCourses = courseRepository.findByStatus("ACTIVE");
        List<Course> inactiveCourses = courseRepository.findByStatus("INACTIVE");

        assertThat(activeCourses).hasSize(1);
        assertThat(inactiveCourses).hasSize(1);
    }

    @Test
    //para este test se crean dos cursos con estado activo diferente, luego se verifica si se obtienen ambos cursos al buscar por estado activo
    void shouldFindByActive(){
        Instructor instructor = createInstructor();
        Course course1 = createCourse(instructor);
        Course course2 = createCourse(instructor);

        course1.setActive(false);;
        courseRepository.save(course1);

        course2.setActive(true);
        courseRepository.save(course2);

        List<Course> activeCourses = courseRepository.findByActive(true);

        assertThat(activeCourses).hasSize(1);
    }

    @Test
    //para este test se crean dos cursos con títulos diferentes, luego se verifica si se obtiene el curso correcto al buscar por una palabra clave en el título
    void shouldFindByTitleContainingIgnorecase() {
        Instructor instructor = createInstructor();
        Course course1 = createCourse(instructor);
        Course course2 = createCourse(instructor);

        course1.setTitle("Java Programming");
        courseRepository.save(course1);

        course2.setTitle("Python Programming");
        courseRepository.save(course2);

        List<Course> javaCourses = courseRepository.findByTitleContainingIgnoreCase("Java");
        List<Course> pythonCourses = courseRepository.findByTitleContainingIgnoreCase("Python");

        assertThat(javaCourses).hasSize(1);
        assertThat(javaCourses.get(0).getTitle()).isEqualTo("Java Programming");

        assertThat(pythonCourses).hasSize(1);
        assertThat(pythonCourses.get(0).getTitle()).isEqualTo("Python Programming");

    }

    @Test
    // Para este test se crea un instructor con dos cursos, uno activo y uno inactivo, luego se verifica si se obtiene solo el curso activo al buscar por instructor y active=true
    void shouldFindByInstructorIdAndActive() {
        Instructor instructor = createInstructor();
        Course course1 = createCourse(instructor);
        Course course2 = createCourse(instructor);

        course1.setActive(false);
        courseRepository.save(course1);

        course2.setActive(true);
        courseRepository.save(course2);

        List<Course> activeCourses = courseRepository.findByInstructorIdAndActive(instructor.getId(), true);

        assertThat(activeCourses).hasSize(1);
        assertThat(activeCourses.get(0).getId()).isEqualTo(course2.getId());
    }

    @Test
    // Para este test se crea un curso con lecciones, luego se verifica si se obtiene el curso con las lecciones cargadas
    void shouldFindByIdWithLessons() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson1 = createLesson(course);
        Lesson lesson2 = createLesson(course);

        Optional<Course> result = courseRepository.findByIdWithLessons(course.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getLessons()).hasSize(2);
        assertThat(result.get().getLessons()).extracting("id").containsExactlyInAnyOrder(lesson1.getId(), lesson2.getId());
    }

    @Test
    // Para este test se crea un instructor con cursos activos, luego se verifica si se obtienen los cursos activos ordenados por fecha de creación descendente
    void shouldFindActiveCoursesByInstructor() {
        Instructor instructor = createInstructor();
        Course course1 = createCourse(instructor);
        Course course2 = createCourse(instructor);

        course1.setActive(true);
        courseRepository.save(course1);

        course2.setActive(false);
        courseRepository.save(course2);

        List<Course> activeCourses = courseRepository.findActiveCoursesByInstructor(instructor.getId());

        assertThat(activeCourses).hasSize(1);
        assertThat(activeCourses.get(0).getId()).isEqualTo(course1.getId());
    }

    @Test
    // Para este test se crean cursos con diferentes estados, luego se verifica el conteo por estado
    void shouldCountCoursesByStatus() {
        Instructor instructor = createInstructor();
        Course course1 = createCourse(instructor);
        Course course2 = createCourse(instructor);

        course1.setStatus("ACTIVE");
        courseRepository.save(course1);

        course2.setStatus("INACTIVE");
        courseRepository.save(course2);

        List<Object[]> result = courseRepository.countCoursesByStatus();

        assertThat(result).hasSize(2);
        // Verificar que contiene los conteos correctos
        boolean hasActive = false, hasInactive = false;
        for (Object[] row : result) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            if ("ACTIVE".equals(status)) {
                assertThat(count).isEqualTo(1L);
                hasActive = true;
            } else if ("INACTIVE".equals(status)) {
                assertThat(count).isEqualTo(1L);
                hasInactive = true;
            }
        }
        assertThat(hasActive && hasInactive).isTrue();
    }

    @Test
    // Para este test se crean cursos con una leccion, luego se verifica si se obtienen solo los cursos que tienen lecciones
    void shouldFindCoursesWithLessons() {
        Instructor instructor = createInstructor();
        Course courseWithLessons = createCourse(instructor);
        Lesson lesson = createLesson(courseWithLessons);

        courseWithLessons.addLesson(lesson);

        List<Course> coursesWithLessons = courseRepository.findCoursesWithLessons();

        assertThat(coursesWithLessons).hasSize(1);
        assertThat(coursesWithLessons.get(0).getId()).isEqualTo(courseWithLessons.getId());
    }


}
