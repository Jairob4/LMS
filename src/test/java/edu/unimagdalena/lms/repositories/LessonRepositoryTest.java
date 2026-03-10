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

class LessonRepositoryTest extends AbstractIntegrationDBTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

<<<<<<< HEAD
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AssesmentRepository assesmentRepository;

    @BeforeEach
    void clean() {
        assesmentRepository.deleteAll();
        lessonRepository.deleteAll();
        courseRepository.deleteAll();
        instructorRepository.deleteAll();
        studentRepository.deleteAll();
=======

    @BeforeEach
    void clean() {
        lessonRepository.deleteAll();
        courseRepository.deleteAll();
        instructorRepository.deleteAll();
>>>>>>> pruebas
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

    private Lesson createLesson(Course course, int orderIndex) {
        Lesson lesson = Lesson.builder()
                .course(course)
                .title("Lesson " + UUID.randomUUID())
                .orderIndex(orderIndex)
                .build();

        return lessonRepository.save(lesson);
    }

<<<<<<< HEAD
    private Student createStudent() {
        Student student = Student.builder()
                .email("student" + UUID.randomUUID() + "@test.com")
                .fullName("Student Test")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return studentRepository.save(student);
    }

    private Assesment createAssesment(Student student, Lesson lesson, int score) {
        Assesment assesment = Assesment.builder()
                .student(student)
                .lesson(lesson)
                .score(score)
                .type("quiz")
                .takenAt(Instant.now())
                .build();

        return assesmentRepository.save(assesment);
    }

=======
>>>>>>> pruebas
    @Test
    // Para este test se crea un curso con lecciones, luego se verifica si se obtienen las lecciones por courseId
    void shouldFindByCourseId() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson1 = createLesson(course, 1);
        Lesson lesson2 = createLesson(course, 2);

        List<Lesson> lessons = lessonRepository.findByCourseId(course.getId());

        assertThat(lessons).hasSize(2);
        assertThat(lessons).extracting("id").containsExactlyInAnyOrder(lesson1.getId(), lesson2.getId());
    }

    @Test
    // Para este test se crea una lección, luego se verifica si se encuentra por título exacto
    void shouldFindByTitle() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course, 1);

        Optional<Lesson> result = lessonRepository.findByTitle(lesson.getTitle());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(lesson.getId());
    }

    @Test
    // Para este test se crean lecciones con títulos diferentes, luego se verifica la búsqueda por título que contenga una cadena
    void shouldFindByTitleContainingIgnoreCase() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson1 = createLesson(course, 1);
        lesson1.setTitle("Java Basics");
        lessonRepository.save(lesson1);

        Lesson lesson2 = createLesson(course, 2);
        lesson2.setTitle("Python Intro");
        lessonRepository.save(lesson2);

        List<Lesson> javaLessons = lessonRepository.findByTitleContainingIgnoreCase("Java");

        assertThat(javaLessons).hasSize(1);
        assertThat(javaLessons.get(0).getTitle()).isEqualTo("Java Basics");
    }

    @Test
    // Para este test se crea un curso con lecciones, luego se verifica si se encuentra la lección por courseId y orderIndex
    void shouldFindByCourseIdAndOrderIndex() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course, 1);

        Optional<Lesson> result = lessonRepository.findByCourseIdAndOrderIndex(course.getId(), 1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(lesson.getId());
    }

    @Test
    // Para este test se crea un curso con lecciones, luego se verifica si se obtienen las lecciones ordenadas por orderIndex
    void shouldFindByCourseIdOrderByIndex() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson1 = createLesson(course, 2);
        Lesson lesson2 = createLesson(course, 1);

        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByIndex(course.getId());

        assertThat(lessons).hasSize(2);
        assertThat(lessons.get(0).getOrderIndex()).isEqualTo(1);
        assertThat(lessons.get(1).getOrderIndex()).isEqualTo(2);
    }

    @Test
    // Para este test se crea un curso con lecciones, luego se verifica el conteo de lecciones por courseId
    void shouldCountByCourseId() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        createLesson(course, 1);
        createLesson(course, 2);

        long count = lessonRepository.countByCourseId(course.getId());

        assertThat(count).isEqualTo(2L);
    }

    @Test
    // Para este test se crea un curso con lecciones, luego se verifica si se obtiene la última lección por orderIndex
    void shouldFindLastLessonByCourseId() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        createLesson(course, 1);
        Lesson lastLesson = createLesson(course, 3);

        Optional<Lesson> result = lessonRepository.findLastLessonByCourseId(course.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(lastLesson.getId());
    }

    @Test
<<<<<<< HEAD
    // Para este test se crea una lección con evaluaciones, luego se verifica si se encuentra en la lista de lecciones con evaluaciones
    void shouldFindLessonsWithAssesments() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course, 1);
        Student student = createStudent();

        createAssesment(student, lesson, 80);

        List<Lesson> lessonsWithAssesments = lessonRepository.findLessonsWithAssesments();

        assertThat(lessonsWithAssesments).hasSize(1);
        assertThat(lessonsWithAssesments.get(0).getId()).isEqualTo(lesson.getId());
=======
    // Para este test se crea una lección con un curso, luego se verifica si se encuentra en la lista de lecciones con cursos
    void shouldFindLessonsWithCourse() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course, 1);

        List<Lesson> lessonsWithCourse = lessonRepository.findLessonsWithCourse();

        assertThat(lessonsWithCourse).hasSize(1);
        assertThat(lessonsWithCourse.get(0).getId()).isEqualTo(lesson.getId());
        assertThat(lessonsWithCourse.get(0).getCourse()).isNotNull();
        assertThat(lessonsWithCourse.get(0).getCourse().getId()).isEqualTo(course.getId());
>>>>>>> pruebas
    }
}