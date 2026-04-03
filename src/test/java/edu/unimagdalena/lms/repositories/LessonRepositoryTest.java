package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.domine.entities.*;
import edu.unimagdalena.lms.domine.repositories.CourseRepository;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;
import edu.unimagdalena.lms.domine.repositories.LessonRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
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


    @BeforeEach
    void clean() {
        lessonRepository.deleteAll();
        courseRepository.deleteAll();
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

    @Test
    // Para este test se crea un curso con lecciones, luego se verifica si se obtienen las lecciones por courseId
    void shouldFindByCourseId() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson1 = createLesson(course, 1);
        Lesson lesson2 = createLesson(course, 2);

        Page<Lesson> lessons = lessonRepository.findByCourseId(course.getId(), PageRequest.of(0, 10));

        assertThat(lessons.getTotalElements()).isEqualTo(2);
        assertThat(lessons.getContent()).extracting("id").containsExactlyInAnyOrder(lesson1.getId(), lesson2.getId());
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

        Page<Lesson> javaLessons = lessonRepository.findByTitleContainingIgnoreCase("Java", PageRequest.of(0, 10));

        assertThat(javaLessons.getTotalElements()).isEqualTo(1);
        assertThat(javaLessons.getContent().get(0).getTitle()).isEqualTo("Java Basics");
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

        Page<Lesson> lessons = lessonRepository.findByCourseIdOrderByIndex(course.getId(), PageRequest.of(0, 10));

        assertThat(lessons.getTotalElements()).isEqualTo(2);
        assertThat(lessons.getContent().get(0).getOrderIndex()).isEqualTo(1);
        assertThat(lessons.getContent().get(1).getOrderIndex()).isEqualTo(2);
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
    // Para este test se crea una lección con un curso, luego se verifica si se encuentra en la lista de lecciones con cursos
    void shouldFindLessonsWithCourse() {
        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course, 1);

        Page<Lesson> lessonsWithCourse = lessonRepository.findLessonsWithCourse(PageRequest.of(0, 10));

        assertThat(lessonsWithCourse.getTotalElements()).isEqualTo(1);
        assertThat(lessonsWithCourse.getContent().get(0).getId()).isEqualTo(lesson.getId());
        assertThat(lessonsWithCourse.getContent().get(0).getCourse()).isNotNull();
        assertThat(lessonsWithCourse.getContent().get(0).getCourse().getId()).isEqualTo(course.getId());
    }
}