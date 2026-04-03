package edu.unimagdalena.lms.repositories;

import edu.unimagdalena.lms.domine.entities.*;
import edu.unimagdalena.lms.domine.repositories.AssessmentRepository;
import edu.unimagdalena.lms.domine.repositories.EnrollmentRepository;
import edu.unimagdalena.lms.domine.repositories.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StudentRepositoryTest extends AbstractIntegrationDBTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AssessmentRepository assesmentRepository;

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

    private Assessment createAssesment(Student student, int score) {
        Assessment assesment = Assessment.builder()
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

        Page<Student> result = studentRepository.findByFullNameContaining("Juan", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getFullName()).isEqualTo("Juan Perez");
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

        Page<Student> result = studentRepository.findStudentsWithEnrollmentStatus("ACTIVE", PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(student.getId());
    }

    @Test
    // Verifica estudiantes que han presentado evaluaciones
    void shouldFindStudentsWithAssesments() {
        Student student = createStudent();
        createAssesment(student, 90);

        Page<Student> result = studentRepository.findStudentsWithAssessments(PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(student.getId());
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