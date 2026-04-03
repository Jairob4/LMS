package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.CourseDtos.CourseCreateRequest;
import edu.unimagdalena.lms.api.dto.CourseDtos.CourseResponse;
import edu.unimagdalena.lms.api.dto.CourseDtos.CourseUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Course;
import edu.unimagdalena.lms.domine.entities.Instructor;
import edu.unimagdalena.lms.domine.repositories.CourseRepository;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.CourseMapper;
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
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

	@Mock
	private CourseRepository courseRepository;
	@Mock
	private InstructorRepository instructorRepository;
	@Mock
	private CourseMapper courseMapper;

	@InjectMocks
	private CourseServiceImpl courseService;

	@Test
	void shouldCreateCourseSuccessfully() {
		var instructorId = UUID.randomUUID();
		var courseId = UUID.randomUUID();
		var request = new CourseCreateRequest("Math", "ACTIVE", true, Instant.now(), Instant.now());

		var instructor = Instructor.builder()
				.id(instructorId)
				.email("inst@test.com")
				.fullName("Prof Test")
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		var course = Course.builder()
				.title("Math")
				.status("ACTIVE")
				.active(true)
				.build();

		var response = new CourseResponse(
				courseId,
				"Math",
				"ACTIVE",
				true,
				Instant.now(),
				Instant.now(),
				instructorId,
				Set.of(),
				Set.of(),
				Set.of()
		);

		when(courseMapper.toEntity(request)).thenReturn(course);
		when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
		when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
			Course c = invocation.getArgument(0);
			c.setId(courseId);
			return c;
		});
		when(courseMapper.toResponse(any(Course.class))).thenReturn(response);

		var result = courseService.createCourse(request, instructorId);

		assertEquals(courseId, result.id());
		assertEquals("Math", result.title());
		verify(courseRepository).save(any(Course.class));
	}

	@Test
	void shouldThrowWhenCreateCourseInstructorNotFound() {
		var instructorId = UUID.randomUUID();
		var request = new CourseCreateRequest("Math", "ACTIVE", true, Instant.now(), Instant.now());
		var course = Course.builder().title("Math").status("ACTIVE").active(true).build();

		when(courseMapper.toEntity(request)).thenReturn(course);
		when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> courseService.createCourse(request, instructorId));
		verify(courseRepository, never()).save(any());
	}

	@Test
	void shouldGetCourseSuccessfully() {
		var id = UUID.randomUUID();
		var instructorId = UUID.randomUUID();
		var course = Course.builder().id(id).title("Math").status("ACTIVE").active(true).build();
		var response = new CourseResponse(id, "Math", "ACTIVE", true, Instant.now(), Instant.now(), instructorId, Set.of(), Set.of(), Set.of());

		when(courseRepository.findById(id)).thenReturn(Optional.of(course));
		when(courseMapper.toResponse(course)).thenReturn(response);

		var result = courseService.getCourse(id);
		assertEquals(id, result.id());
	}

	@Test
	void shouldThrowWhenGetCourseNotFound() {
		var id = UUID.randomUUID();
		when(courseRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> courseService.getCourse(id));
	}

	@Test
	void shouldUpdateCourseSuccessfully() {
		var id = UUID.randomUUID();
		var request = new CourseUpdateRequest("Physics", "INACTIVE", false);
		var existing = Course.builder().id(id).title("Math").status("ACTIVE").active(true).build();
		var response = new CourseResponse(id, "Physics", "INACTIVE", false, Instant.now(), Instant.now(), UUID.randomUUID(), Set.of(), Set.of(), Set.of());

		when(courseRepository.findById(id)).thenReturn(Optional.of(existing));
		when(courseRepository.save(existing)).thenReturn(existing);
		when(courseMapper.toResponse(existing)).thenReturn(response);

		var result = courseService.updateCourse(id, request);

		assertEquals(id, result.id());
		verify(courseMapper).patch(existing, request);
		verify(courseRepository).save(existing);
	}

	@Test
	void shouldThrowWhenUpdateCourseNotFound() {
		var id = UUID.randomUUID();
		var request = new CourseUpdateRequest("Physics", "INACTIVE", false);

		when(courseRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> courseService.updateCourse(id, request));
		verify(courseRepository, never()).save(any());
	}

	@Test
	void shouldDeleteCourseSuccessfully() {
		var id = UUID.randomUUID();
		var course = Course.builder().id(id).build();

		when(courseRepository.findById(id)).thenReturn(Optional.of(course));

		courseService.delete(id);

		verify(courseRepository).delete(course);
	}

	@Test
	void shouldThrowWhenDeleteCourseNotFound() {
		var id = UUID.randomUUID();
		when(courseRepository.findById(id)).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> courseService.delete(id));
	}

	@Test
	void shouldFindByStatus() {
		var pageable = PageRequest.of(0, 10);
		var course = Course.builder().id(UUID.randomUUID()).title("Math").status("ACTIVE").active(true).build();
		var dto = new CourseResponse(course.getId(), "Math", "ACTIVE", true, Instant.now(), Instant.now(), UUID.randomUUID(), Set.of(), Set.of(), Set.of());
		var page = new PageImpl<>(List.of(course), pageable, 1);

		when(courseRepository.findByStatus("ACTIVE", pageable)).thenReturn(page);
		when(courseMapper.toResponse(course)).thenReturn(dto);

		var result = courseService.findByStatus("ACTIVE", pageable);

		assertThat(result.getTotalElements()).isEqualTo(1);
		assertEquals(course.getId(), result.getContent().get(0).id());
	}

	@Test
	void shouldActivateCourse() {
		var id = UUID.randomUUID();
		var course = Course.builder().id(id).active(false).build();

		when(courseRepository.findById(id)).thenReturn(Optional.of(course));

		courseService.activateCourse(id);

		assertTrue(course.isActive());
		verify(courseRepository).save(course);
	}

	@Test
	void shouldDeactivateCourse() {
		var id = UUID.randomUUID();
		var course = Course.builder().id(id).active(true).build();

		when(courseRepository.findById(id)).thenReturn(Optional.of(course));

		courseService.desactivateCourse(id);

		assertFalse(course.isActive());
		verify(courseRepository).save(course);
	}

	@Test
	void shouldReturnIsCourseActive() {
		var id = UUID.randomUUID();
		var course = Course.builder().id(id).active(true).build();
		when(courseRepository.findById(id)).thenReturn(Optional.of(course));

		var result = courseService.isCourseActive(id);
		assertTrue(result);
	}

	@Test
	void shouldThrowWhenIsCourseActiveCourseNotFound() {
		var id = UUID.randomUUID();
		when(courseRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> courseService.isCourseActive(id));
	}
}
