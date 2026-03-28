package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.domine.entities.*;
import edu.unimagdalena.lms.domine.repositories.AssesmentRepository;
import edu.unimagdalena.lms.domine.repositories.CourseRepository;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;
import edu.unimagdalena.lms.domine.repositories.StudentRepository;

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

    

    @BeforeEach
    void clean() {
        assesmentRepository.deleteAll();
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

    private Assesment createAssesment(Student student, Course course, int score, String type) {

        Assesment assesment = Assesment.builder()
                .student(student)
                .course(course)
                .score(score)
                .type(type)
                .takenAt(Instant.now())
                .build();

        return assesmentRepository.save(assesment);
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para cada estudiante en ese curso, luego se verifica si al buscar por el id del estudiante se obtienen ambas evaluaciones
    void shouldFindByStudentId() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssesment(student, course, 80, "quiz");
        createAssesment(student, course, 90, "exam");

        List<Assesment> result =
                assesmentRepository.findByStudentId(student.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el id del curso se obtienen ambas evaluaciones
    void shouldFindByCourseId() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssesment(student, course, 80, "quiz");
        createAssesment(student, course, 90, "exam");

        List<Assesment> result =
                assesmentRepository.findByCourseId(course.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el tipo de evaluación se obtiene la evaluación correcta
    void shouldFindByType() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssesment(student, course, 85, "quiz");
        createAssesment(student, course, 95, "exam");

        List<Assesment> result =
                assesmentRepository.findByType("quiz");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("quiz");
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el puntaje mayor o igual a un valor se obtiene la evaluación correcta
    void shouldFindByScoreGreaterThanEqual() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssesment(student, course, 60, "quiz");
        createAssesment(student, course, 90, "exam");

        List<Assesment> result =
                assesmentRepository.findByScoreGreaterThanEqual(80);

        assertThat(result).hasSize(1);
    }

    @Test
    // en este test se crea un instructor, un curso, un estudiante y una evaluación para ese curso, luego se verifica si al buscar por las evaluaciones entre dos fechas se obtiene la evaluación correcta
    void shouldFindByTakenAtBetween() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        Instant now = Instant.now();

        createAssesment(student, course, 75, "quiz");

        List<Assesment> result =
                assesmentRepository.findByTakenAtBetween(
                        now.minusSeconds(60),
                        now.plusSeconds(60)
                );

        assertThat(result).hasSize(1);
    }

    @Test
    // Para este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar el puntaje promedio por el id del curso se obtiene el valor correcto
    void shouldCalculateAverageScoreByLesson() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssesment(student, course, 80, "quiz");
        createAssesment(student, course, 100, "quiz");

        Double avg =
                assesmentRepository.findAverageScoreByCourseId(course.getId());

        assertThat(avg).isEqualTo(90.0);
    }

    @Test
    // Para este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el id del estudiante y el id del curso se obtienen ambas evaluaciones
    void shouldFindByStudentAndCourseId() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssesment(student, course, 85, "quiz");

        List<Assesment> result =
                assesmentRepository.findByStudentIdAndCourseId(
                        student.getId(),
                        course.getId()
                );

        assertThat(result).hasSize(1);
    }

    @Test
    // Para este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar por el id del estudiante y un puntaje mínimo se obtiene la evaluación correcta
    void shouldFindPassingAssesmentsByStudent() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssesment(student, course, 60, "quiz");
        createAssesment(student, course, 95, "exam");

        List<Assesment> result =
                assesmentRepository.findPassingAssesmentsByStudent(
                        student.getId(),
                        80
                );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScore()).isGreaterThanOrEqualTo(80);
    }

    @Test
    // Para este test se crea un instructor, un curso, un estudiante y 2 evaluaciones para ese curso, luego se verifica si al buscar el puntaje máximo por tipo de evaluación se obtiene el valor correcto
    void shouldFindMaxScoreByType() {

        Instructor instructor = createInstructor();
        Course course = createCourse(instructor);
        Student student = createStudent();

        createAssesment(student, course, 80, "quiz");
        createAssesment(student, course, 95, "quiz");

        List<Object[]> result = assesmentRepository.findMaxScoreByType();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)[0]).isEqualTo("quiz");
        assertThat(result.get(0)[1]).isEqualTo(95);
    }
}