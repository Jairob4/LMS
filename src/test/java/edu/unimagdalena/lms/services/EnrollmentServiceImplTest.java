package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.CourseEnrollmentStatsResponse;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentCreateRequest;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentResponse;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Course;
import edu.unimagdalena.lms.domine.entities.Enrollment;
import edu.unimagdalena.lms.domine.entities.Student;
import edu.unimagdalena.lms.domine.repositories.CourseRepository;
import edu.unimagdalena.lms.domine.repositories.EnrollmentRepository;
import edu.unimagdalena.lms.domine.repositories.StudentRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.EnrollmentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

	@Mock
	private EnrollmentRepository enrollmentRepository;
	@Mock
	private EnrollmentMapper enrollmentMapper;
	@Mock
	private StudentRepository studentRepository;
	@Mock
	private CourseRepository courseRepository;

	@InjectMocks
	private EnrollmentServiceImpl enrollmentService;

	@Test
	void shouldCreateEnrollmentSuccessfully() {
		var studentId = UUID.randomUUID();
		var courseId = UUID.randomUUID();
		var enrollmentId = UUID.randomUUID();
		var now = Instant.now();

		var request = new EnrollmentCreateRequest(studentId, courseId, "ACTIVE", now);
		var student = Student.builder().id(studentId).email("s@test.com").fullName("Student").createdAt(now).updatedAt(now).build();
		var course = Course.builder().id(courseId).title("Math").status("ACTIVE").active(true).createdAt(now).updatedAt(now).build();
		var enrollment = Enrollment.builder().status("ACTIVE").enrolledAt(now).build();
		var response = new EnrollmentResponse(enrollmentId, studentId, courseId, "ACTIVE", now);

		when(enrollmentMapper.toEntity(request)).thenReturn(enrollment);
		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(i -> {
			Enrollment e = i.getArgument(0);
			e.setId(enrollmentId);
			return e;
		});
		when(enrollmentMapper.toResponse(any(Enrollment.class))).thenReturn(response);

		var result = enrollmentService.createEnrollment(request, studentId, courseId);

		assertEquals(enrollmentId, result.id());
		verify(enrollmentRepository).save(any(Enrollment.class));
	}

	@Test
	void shouldThrowWhenCreateStudentNotFound() {
		var studentId = UUID.randomUUID();
		var courseId = UUID.randomUUID();
		var request = new EnrollmentCreateRequest(studentId, courseId, "ACTIVE", Instant.now());
		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> enrollmentService.createEnrollment(request, studentId, courseId));
		verify(courseRepository, never()).findById(any());
	}

	@Test
	void shouldGetEnrollmentSuccessfully() {
		var id = UUID.randomUUID();
		var enrollment = Enrollment.builder().id(id).status("ACTIVE").enrolledAt(Instant.now()).build();
		var response = new EnrollmentResponse(id, UUID.randomUUID(), UUID.randomUUID(), "ACTIVE", enrollment.getEnrolledAt());

		when(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment));
		when(enrollmentMapper.toResponse(enrollment)).thenReturn(response);

		var result = enrollmentService.getEnrollment(id);
		assertEquals(id, result.id());
	}

	@Test
	void shouldThrowWhenGetEnrollmentNotFound() {
		var id = UUID.randomUUID();
		when(enrollmentRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> enrollmentService.getEnrollment(id));
	}

	@Test
	void shouldUpdateEnrollmentSuccessfully() {
		var id = UUID.randomUUID();
		var req = new EnrollmentUpdateRequest("INACTIVE", Instant.now());
		var enrollment = Enrollment.builder().id(id).status("ACTIVE").enrolledAt(Instant.now()).build();
		var response = new EnrollmentResponse(id, UUID.randomUUID(), UUID.randomUUID(), "INACTIVE", req.enrolledAt());

		when(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment));
		when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
		when(enrollmentMapper.toResponse(enrollment)).thenReturn(response);

		var result = enrollmentService.updateEnrollment(id, req);
		assertEquals(id, result.id());
		verify(enrollmentMapper).patch(enrollment, req);
	}

	@Test
	void shouldDeleteEnrollmentSuccessfully() {
		var id = UUID.randomUUID();
		var enrollment = Enrollment.builder().id(id).build();
		when(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment));

		enrollmentService.delete(id);
		verify(enrollmentRepository).delete(enrollment);
	}

	@Test
	void shouldFindByCourseId() {
		var courseId = UUID.randomUUID();
		var pageable = PageRequest.of(0, 10);
		var enrollment = Enrollment.builder().id(UUID.randomUUID()).status("ACTIVE").enrolledAt(Instant.now()).build();
		var dto = new EnrollmentResponse(enrollment.getId(), UUID.randomUUID(), courseId, "ACTIVE", enrollment.getEnrolledAt());
		var page = new PageImpl<>(List.of(enrollment), pageable, 1);

		when(enrollmentRepository.findByCourseId(courseId, pageable)).thenReturn(page);
		when(enrollmentMapper.toResponse(enrollment)).thenReturn(dto);

		var result = enrollmentService.findByCourseId(courseId, pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void shouldMapCountEnrollmentsByCourse() {
		var pageable = PageRequest.of(0, 10);
		var courseId = UUID.randomUUID();
		Page<Object[]> rows = new PageImpl<>(Collections.singletonList(new Object[]{courseId, "Math", 5L}), pageable, 1);
		when(enrollmentRepository.countEnrollmentsByCourse(pageable)).thenReturn(rows);

		var result = enrollmentService.countEnrollmentsByCourse(pageable);

		assertThat(result.getTotalElements()).isEqualTo(1);
		CourseEnrollmentStatsResponse stats = result.getContent().get(0);
		assertEquals(courseId, stats.courseId());
		assertEquals("Math", stats.courseTitle());
		assertEquals(5L, stats.totalEnrollments());
	}
}
