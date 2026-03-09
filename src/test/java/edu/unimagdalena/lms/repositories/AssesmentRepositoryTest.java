package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class AssesmentRepositoryTest extends AbstractIntegrationDBTest {

    @Autowired
    private AssesmentRepository assesmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @BeforeEach
    void clean() {
        assesmentRepository.deleteAll();
        lessonRepository.deleteAll();
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

    private Lesson createLesson(Course course) {

        Lesson lesson = Lesson.builder()
                .course(course)
                .title("Lesson " + UUID.randomUUID())
                .orderIndex(1)
                .build();

        return lessonRepository.save(lesson);
    }

    private Student createStudent() {

        Student student = Student.builder()
                .email("student" + UUID.randomUUID() + "@test.com")
                .fullName("Student Test")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return studentRepository.save(student);
    }

    private Assesment createAssesment(Student student, Lesson lesson, int score, String type) {

        Assesment assesment = Assesment.builder()
                .student(student)
                .lesson(lesson)
                .score(score)
                .type(type)
                .takenAt(Instant.now())
                .build();

        return assesmentRepository.save(assesment);
    }

    @Test
    void shouldFindByStudentId() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        createAssesment(student, lesson, 80, "quiz");
        createAssesment(student, lesson, 90, "exam");

        List<Assesment> result =
                assesmentRepository.findByStudentId(student.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldFindByLessonId() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        createAssesment(student, lesson, 70, "quiz");

        List<Assesment> result =
                assesmentRepository.findByLessonId(lesson.getId());

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindByType() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        createAssesment(student, lesson, 85, "quiz");
        createAssesment(student, lesson, 95, "exam");

        List<Assesment> result =
                assesmentRepository.findByType("quiz");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("quiz");
    }

    @Test
    void shouldFindByScoreGreaterThanEqual() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        createAssesment(student, lesson, 60, "quiz");
        createAssesment(student, lesson, 90, "exam");

        List<Assesment> result =
                assesmentRepository.findByScoreGreaterThanEqual(80);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindByTakenAtBetween() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        Instant now = Instant.now();

        createAssesment(student, lesson, 75, "quiz");

        List<Assesment> result =
                assesmentRepository.findByTakenAtBetween(
                        now.minusSeconds(60),
                        now.plusSeconds(60)
                );

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldCalculateAverageScoreByLesson() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        createAssesment(student, lesson, 80, "quiz");
        createAssesment(student, lesson, 100, "quiz");

        Double avg =
                assesmentRepository.findAverageScoreByLessonId(lesson.getId());

        assertThat(avg).isEqualTo(90.0);
    }

    @Test
    void shouldFindByStudentAndLesson() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        createAssesment(student, lesson, 85, "quiz");

        List<Assesment> result =
                assesmentRepository.findByStudentIdAndLessonId(
                        student.getId(),
                        lesson.getId()
                );

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindPassingAssesmentsByStudent() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        createAssesment(student, lesson, 60, "quiz");
        createAssesment(student, lesson, 95, "exam");

        List<Assesment> result =
                assesmentRepository.findPassingAssesmentsByStudent(
                        student.getId(),
                        80
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScore()).isGreaterThanOrEqualTo(80);
    }

    @Test
    void shouldFindMaxScoreByType() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Lesson lesson = createLesson(course);
        Student student = createStudent();

        createAssesment(student, lesson, 80, "quiz");
        createAssesment(student, lesson, 95, "quiz");

        List<Object[]> result = assesmentRepository.findMaxScoreByType();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)[0]).isEqualTo("quiz");
        assertThat(result.get(0)[1]).isEqualTo(95);
    }
}