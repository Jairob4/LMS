package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentCreateRequest;
import edu.unimagdalena.lms.api.dto.AssessmentDtos.AssessmentResponse;
import edu.unimagdalena.lms.domine.entities.Assessment;
import edu.unimagdalena.lms.domine.entities.Course;
import edu.unimagdalena.lms.domine.entities.Student;
import edu.unimagdalena.lms.domine.repositories.AssessmentRepository;
import edu.unimagdalena.lms.domine.repositories.CourseRepository;
import edu.unimagdalena.lms.domine.repositories.StudentRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.AssessmentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceImplTest {

	@Mock
	private AssessmentRepository assessmentRepository;
	@Mock
	private StudentRepository studentRepository;
	@Mock
	private CourseRepository courseRepository;

	@Mock
	private AssessmentMapper assessmentMapper;

	@InjectMocks
	private AssessmentServiceImpl assessmentService;

	@Test
	void shouldCreateAssessmentSuccessfully() {
		var studentId = UUID.randomUUID();
		var courseId = UUID.randomUUID();
		var assessmentId = UUID.randomUUID();
		var takenAt = Instant.now();

		var request = new AssessmentCreateRequest("Quiz", 90, takenAt);
		var student = Student.builder().id(studentId).email("john@example.com").fullName("John Doe").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var course = Course.builder().id(courseId).title("Math").status("ACTIVE").active(true).createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var assessment = Assessment.builder().type("Quiz").score(90).takenAt(takenAt).build();
		var responseDto = new AssessmentResponse(assessmentId, studentId, courseId, "Quiz", 90, takenAt);

		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(assessmentMapper.toEntity(request)).thenReturn(assessment);
		when(assessmentMapper.toResponse(any(Assessment.class))).thenReturn(responseDto);
		when(assessmentRepository.save(any(Assessment.class))).thenAnswer(invocation -> {
			Assessment a = invocation.getArgument(0);
			a.setId(assessmentId);
			return a;
		});

		var response = assessmentService.createAssessment(request, studentId, courseId);

		assertEquals(assessmentId, response.id());
		assertEquals("Quiz", response.type());
		assertEquals(90, response.score());
		assertEquals(studentId, response.studentId());
		assertEquals(courseId, response.courseId());
		verify(assessmentRepository).save(any(Assessment.class));
	}

	@Test
	void shouldThrowWhenCreateRequestHasBlankType() {
		var request = new AssessmentCreateRequest("  ", 90, Instant.now());

		var ex = assertThrows(IllegalArgumentException.class,
				() -> assessmentService.createAssessment(request, UUID.randomUUID(), UUID.randomUUID()));

		assertTrue(ex.getMessage().contains("type"));
		verify(studentRepository, never()).findById(any());
		verify(courseRepository, never()).findById(any());
		verify(assessmentRepository, never()).save(any());
	}

	@Test
	void shouldThrowWhenCreateRequestHasInvalidScore() {
		var request = new AssessmentCreateRequest("Quiz", 150, Instant.now());

		assertThrows(IllegalArgumentException.class,
				() -> assessmentService.createAssessment(request, UUID.randomUUID(), UUID.randomUUID()));

		verify(assessmentRepository, never()).save(any());
	}

	@Test
	void shouldThrowWhenStudentNotFoundOnCreate() {
		var studentId = UUID.randomUUID();
		var courseId = UUID.randomUUID();
		var request = new AssessmentCreateRequest("Quiz", 90, Instant.now());

		when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class,
				() -> assessmentService.createAssessment(request, studentId, courseId));

		verify(courseRepository, never()).findById(any());
		verify(assessmentRepository, never()).save(any());
	}

	@Test
	void shouldThrowWhenCourseNotFoundOnCreate() {
		var studentId = UUID.randomUUID();
		var courseId = UUID.randomUUID();
		var request = new AssessmentCreateRequest("Quiz", 90, Instant.now());
		var student = Student.builder().id(studentId).email("john@example.com").fullName("John Doe").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var assessment = Assessment.builder().type("Quiz").score(90).takenAt(request.takenAt()).build();

		when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
		when(courseRepository.findById(courseId)).thenReturn(Optional.empty());
		when(assessmentMapper.toEntity(request)).thenReturn(assessment);

		assertThrows(NotFoundException.class,
				() -> assessmentService.createAssessment(request, studentId, courseId));

		verify(assessmentRepository, never()).save(any());
	}

	@Test
	void shouldGetAssessment() {
		var id = UUID.randomUUID();
		var student = Student.builder().id(UUID.randomUUID()).email("john@example.com").fullName("John Doe").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var course = Course.builder().id(UUID.randomUUID()).title("Math").status("ACTIVE").active(true).createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var assessment = Assessment.builder().id(id).type("Quiz").score(80).takenAt(Instant.now()).student(student).course(course).build();
		var responseDto = new AssessmentResponse(id, student.getId(), course.getId(), "Quiz", 80, assessment.getTakenAt());

		when(assessmentRepository.findById(id)).thenReturn(Optional.of(assessment));
		when(assessmentMapper.toResponse(assessment)).thenReturn(responseDto);

		var response = assessmentService.getAssessment(id);

		assertEquals(id, response.id());
		assertEquals(student.getId(), response.studentId());
		assertEquals(course.getId(), response.courseId());
	}

	@Test
	void shouldThrowWhenGetAssessmentNotFound() {
		var id = UUID.randomUUID();
		when(assessmentRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> assessmentService.getAssessment(id));
	}

	@Test
	void shouldDeleteAssessment() {
		var id = UUID.randomUUID();
		var assessment = Assessment.builder().id(id).type("Quiz").score(80).takenAt(Instant.now()).build();

		when(assessmentRepository.findById(id)).thenReturn(Optional.of(assessment));

		assessmentService.delete(id);

		verify(assessmentRepository).delete(assessment);
	}

	@Test
	void shouldThrowWhenDeleteAssessmentNotFound() {
		var id = UUID.randomUUID();
		when(assessmentRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> assessmentService.delete(id));
	}

	@Test
	void shouldFindByCourseId() {
		var courseId = UUID.randomUUID();
		var pageable = PageRequest.of(0, 10);
		var student = Student.builder().id(UUID.randomUUID()).email("john@example.com").fullName("John Doe").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var course = Course.builder().id(courseId).title("Math").status("ACTIVE").active(true).createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var assessment = Assessment.builder().id(UUID.randomUUID()).type("Quiz").score(80).takenAt(Instant.now()).student(student).course(course).build();
		var dto = new AssessmentResponse(assessment.getId(), student.getId(), course.getId(), assessment.getType(), assessment.getScore(), assessment.getTakenAt());
		var page = new PageImpl<>(List.of(assessment), pageable, 1);

		when(assessmentRepository.findByCourseId(courseId, pageable)).thenReturn(page);
		when(assessmentMapper.toResponse(assessment)).thenReturn(dto);

		var response = assessmentService.findByCourseId(courseId, pageable);

		assertThat(response.getTotalElements()).isEqualTo(1);
		assertEquals(assessment.getId(), response.getContent().get(0).id());
	}

	@Test
	void shouldThrowWhenFindByTakenAtBetweenRangeInvalid() {
		var pageable = PageRequest.of(0, 10);
		var start = Instant.now();
		var end = start.minusSeconds(60);

		assertThrows(IllegalArgumentException.class,
				() -> assessmentService.findByTakenAtBetween(start, end, pageable));

		verify(assessmentRepository, never()).findByTakenAtBetween(any(), any(), any());
	}
}
