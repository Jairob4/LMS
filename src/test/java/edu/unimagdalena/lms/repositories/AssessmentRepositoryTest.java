package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.domine.entities.*;
import edu.unimagdalena.lms.domine.repositories.AssessmentRepository;
import edu.unimagdalena.lms.domine.repositories.CourseRepository;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;
import edu.unimagdalena.lms.domine.repositories.StudentRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class AssessmentRepositoryTest extends AbstractIntegrationDBTest {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    

    @BeforeEach
    void clean() {
        assessmentRepository.deleteAll();
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


    private Student createStudent() {

        Student student = Student.builder()
                .email("student" + UUID.randomUUID() + "@test.com")
                .fullName("Student Test")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return studentRepository.save(student);
    }

    private Assessment createAssessment(Student student, Course course, int score, String type) {

        Assessment assessment = Assessment.builder()
                .student(student)
                .course(course)
                .score(score)
                .type(type)
                .takenAt(Instant.now())
                .build();

        return assessmentRepository.save(assessment);
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para cada estudiante en ese curso, luego se verifica si al buscar por el id del estudiante se obtienen ambas evaluaciones
    void shouldFindByStudentId() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssessment(student, course, 80, "quiz");
        createAssessment(student, course, 90, "exam");

        Page<Assessment> result =
            assessmentRepository.findByStudentId(student.getId(), PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el id del curso se obtienen ambas evaluaciones
    void shouldFindByCourseId() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssessment(student, course, 80, "quiz");
        createAssessment(student, course, 90, "exam");

        Page<Assessment> result =
            assessmentRepository.findByCourseId(course.getId(), PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el tipo de evaluación se obtiene la evaluación correcta
    void shouldFindByType() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssessment(student, course, 85, "quiz");
        createAssessment(student, course, 95, "exam");

        Page<Assessment> result =
            assessmentRepository.findByType("quiz", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getType()).isEqualTo("quiz");
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el puntaje mayor o igual a un valor se obtiene la evaluación correcta
    void shouldFindByScoreGreaterThanEqual() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssessment(student, course, 60, "quiz");
        createAssessment(student, course, 90, "exam");

        Page<Assessment> result =
            assessmentRepository.findByScoreGreaterThanEqual(80, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y una evaluación para ese curso, luego se verifica si al buscar por las evaluaciones entre dos fechas se obtiene la evaluación correcta
    void shouldFindByTakenAtBetween() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        Instant now = Instant.now();

        createAssessment(student, course, 75, "quiz");

        Page<Assessment> result =
                assessmentRepository.findByTakenAtBetween(
                        now.minusSeconds(60),
                now.plusSeconds(60),
                PageRequest.of(0, 10)
                );

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    // Para este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar el puntaje promedio por el id del curso se obtiene el valor correcto
    void shouldCalculateAverageScoreByLesson() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssessment(student, course, 80, "quiz");
        createAssessment(student, course, 100, "quiz");

        Double avg =
                assessmentRepository.findAverageScoreByCourseId(course.getId());

        assertThat(avg).isEqualTo(90.0);
    }

    @Test
    // Para este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el id del estudiante y el id del curso se obtienen ambas evaluaciones
    void shouldFindByStudentAndCourseId() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssessment(student, course, 85, "quiz");

        Page<Assessment> result =
                assessmentRepository.findByStudentIdAndCourseId(
                        student.getId(),
                course.getId(),
                PageRequest.of(0, 10)
                );

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    // Para este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el id del estudiante y un puntaje mínimo se obtiene la evaluación correcta
    void shouldFindPassingAssesmentsByStudent() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssessment(student, course, 60, "quiz");
        createAssessment(student, course, 95, "exam");

        Page<Assessment> result =
                assessmentRepository.findPassingAssessmentsByStudent(
                        student.getId(),
                80,
                PageRequest.of(0, 10)
                );

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getScore()).isGreaterThanOrEqualTo(80);
    }

    @Test
    // Para este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar el puntaje máximo por tipo de evaluación se obtiene el valor correcto
    void shouldFindMaxScoreByType() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssessment(student, course, 80, "quiz");
        createAssessment(student, course, 95, "quiz");

        List<Object[]> result = assessmentRepository.findMaxScoreByType();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)[0]).isEqualTo("quiz");
        assertThat(result.get(0)[1]).isEqualTo(95);
    }
}