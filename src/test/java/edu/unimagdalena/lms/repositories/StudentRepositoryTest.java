package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.entities.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StudentRepositoryTest extends AbstractIntegrationDBTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AssesmentRepository assesmentRepository;

    @BeforeEach
    void clean() {
        assesmentRepository.deleteAll();
        enrollmentRepository.deleteAll();
        studentRepository.deleteAll();
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

    private Enrollment createEnrollment(Student student, String status) {
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .status(status)
                .enrolledAt(Instant.now())
                .build();

        return enrollmentRepository.save(enrollment);
    }

    private Assesment createAssesment(Student student, int score) {
        Assesment assesment = Assesment.builder()
                .student(student)
                .score(score)
                .takenAt(Instant.now())
                .build();

        return assesmentRepository.save(assesment);
    }

    @Test
    // Verifica encontrar estudiante por email
    void shouldFindByEmail() {
        Student student = createStudent();

        Optional<Student> result = studentRepository.findByEmail(student.getEmail());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(student.getId());
    }

    @Test
    // Verifica si existe un estudiante con ese email
    void shouldExistsByEmail() {
        Student student = createStudent();

        boolean exists = studentRepository.existsByEmail(student.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    // Verifica búsqueda por nombre parcial
    void shouldFindByFullNameContaining() {
        Student student = createStudent();
        student.setFullName("Juan Perez");
        studentRepository.save(student);

        List<Student> result = studentRepository.findByFullNameContaining("Juan");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFullName()).isEqualTo("Juan Perez");
    }

    @Test
    // Verifica obtener estudiante con sus matrículas
    void shouldFindByIdWithEnrollments() {
        Student student = createStudent();
        createEnrollment(student, "ACTIVE");

        Optional<Student> result = studentRepository.findByIdWithEnrollments(student.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getEnrollments()).isNotEmpty();
    }

    @Test
    // Verifica estudiantes con matrícula activa
    void shouldFindStudentsWithEnrollmentStatus() {
        Student student = createStudent();
        createEnrollment(student, "ACTIVE");

        List<Student> result = studentRepository.findStudentsWithEnrollmentStatus("ACTIVE");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(student.getId());
    }

    @Test
    // Verifica estudiantes que han presentado evaluaciones
    void shouldFindStudentsWithAssesments() {
        Student student = createStudent();
        createAssesment(student, 90);

        List<Student> result = studentRepository.findStudentsWithAssesments();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(student.getId());
    }

    @Test
    // Verifica cálculo del promedio de puntaje
    void shouldFindAverageScoreByStudentId() {
        Student student = createStudent();
        createAssesment(student, 80);
        createAssesment(student, 100);

        Double average = studentRepository.findAverageScoreByStudentId(student.getId());

        assertThat(average).isEqualTo(90.0);
    }
}