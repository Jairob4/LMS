package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class EnrollmentRepositoryTest extends AbstractIntegrationDBTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void clean() {
        enrollmentRepository.deleteAll();
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

    private Student createStudent() {
        Student student = Student.builder()
                .email("student" + UUID.randomUUID() + "@test.com")
                .fullName("Student Test")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        return studentRepository.save(student);
    }

    private Enrollment createEnrollment(Student student, Instructor instructor, String status) {
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .instructor(instructor)
                .status(status)
                .enrolledAt(Instant.now())
                .build();

        return enrollmentRepository.save(enrollment);
    }

    @Test
    // Para este test se crea un estudiante con matrículas, luego se verifica si se obtienen las matrículas por studentId
    void shouldFindByStudentId() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        Enrollment enrollment = createEnrollment(student, instructor, "ACTIVE");

        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());

        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getId()).isEqualTo(enrollment.getId());
    }

    @Test
    // Para este test se crea un instructor con matrículas, luego se verifica si se obtienen las matrículas por instructorId
    void shouldFindByInstructorId() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        Enrollment enrollment = createEnrollment(student, instructor, "ACTIVE");

        List<Enrollment> enrollments = enrollmentRepository.findByInstructorId(instructor.getId());

        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getId()).isEqualTo(enrollment.getId());
    }

    @Test
    // Para este test se crean matrículas con diferentes estados, luego se verifica la búsqueda por estado
    void shouldFindByStatus() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        Enrollment enrollment1 = createEnrollment(student, instructor, "ACTIVE");
        Enrollment enrollment2 = createEnrollment(student, instructor, "INACTIVE");

        List<Enrollment> activeEnrollments = enrollmentRepository.findByStatus("ACTIVE");

        assertThat(activeEnrollments).hasSize(1);
        assertThat(activeEnrollments.get(0).getId()).isEqualTo(enrollment1.getId());
    }

    @Test
    // Para este test se crea un estudiante con matrículas de diferentes estados, luego se verifica la búsqueda por studentId y status
    void shouldFindByStudentIdAndStatus() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        Enrollment enrollment = createEnrollment(student, instructor, "ACTIVE");

        List<Enrollment> enrollments = enrollmentRepository.findByStudentIdAndStatus(student.getId(), "ACTIVE");

        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getId()).isEqualTo(enrollment.getId());
    }

    @Test
    // Para este test se crean matrículas en diferentes momentos, luego se verifica la búsqueda por enrolledAt después de una fecha
    void shouldFindByEnrolledAtAfter() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        Enrollment enrollment = createEnrollment(student, instructor, "ACTIVE");

        List<Enrollment> enrollments = enrollmentRepository.findByEnrolledAtAfter(Instant.now().minusSeconds(60));

        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getId()).isEqualTo(enrollment.getId());
    }

    @Test
    // Para este test se crea una matrícula, luego se verifica si existe por studentId e instructorId
    void shouldExistsByStudentIdAndInstructorId() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        createEnrollment(student, instructor, "ACTIVE");

        boolean exists = enrollmentRepository.existsByStudentIdAndInstructorId(student.getId(), instructor.getId());

        assertThat(exists).isTrue();
    }

    @Test
    // Para este test se crean matrículas con diferentes estados, luego se verifica el conteo por estado
    void shouldCountEnrollmentsByStatus() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        createEnrollment(student, instructor, "ACTIVE");
        createEnrollment(student, instructor, "INACTIVE");

        List<Object[]> result = enrollmentRepository.countEnrollmentsByStatus();

        assertThat(result).hasSize(2);
        // Verificar conteos
        for (Object[] row : result) {
            String status = (String) row[0];
            Long count = (Long) row[1];
            assertThat(count).isEqualTo(1L);
        }
    }

    @Test
    // Para este test se crea una matrícula con estudiante, luego se verifica si se obtiene la matrícula con estudiante cargado
    void shouldFindEnrollmentsWithStudentByStatus() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        Enrollment enrollment = createEnrollment(student, instructor, "ACTIVE");

        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsWithStudentByStatus("ACTIVE");

        assertThat(enrollments).hasSize(1);
        assertThat(enrollments.get(0).getStudent().getId()).isEqualTo(student.getId());
    }

    @Test
    // Para este test se crea un estudiante con matrículas, luego se verifica el conteo de matrículas por studentId
    void shouldCountByStudentId() {
        Student student = createStudent();
        Instructor instructor = createInstructor();
        createEnrollment(student, instructor, "ACTIVE");
        createEnrollment(student, instructor, "INACTIVE");

        long count = enrollmentRepository.countByStudentId(student.getId());

        assertThat(count).isEqualTo(2L);
    }
}