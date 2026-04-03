package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.LessonDtos.LessonCreateRequest;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonResponse;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Course;
import edu.unimagdalena.lms.domine.entities.Lesson;
import edu.unimagdalena.lms.domine.repositories.CourseRepository;
import edu.unimagdalena.lms.domine.repositories.LessonRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.LessonMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
public class LessonServiceImplTest {

	@Mock
	private LessonRepository lessonRepository;
	@Mock
	private CourseRepository courseRepository;
	@Mock
	private LessonMapper lessonMapper;

	@InjectMocks
	private LessonServiceImpl service;

	@Test
	void shouldCreateLesson() {
		var courseId = UUID.randomUUID();
		var lessonId = UUID.randomUUID();
		var request = new LessonCreateRequest(courseId, "Intro", 1);
		var course = Course.builder().id(courseId).title("Course").status("ACTIVE").active(true).build();
		var lesson = Lesson.builder().title("Intro").orderIndex(1).build();
		var response = new LessonResponse(lessonId, courseId, "Intro", 1);

		when(lessonMapper.toEntity(request)).thenReturn(lesson);
		when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
		when(lessonRepository.save(any(Lesson.class))).thenAnswer(i -> {
			Lesson l = i.getArgument(0);
			l.setId(lessonId);
			return l;
		});
		when(lessonMapper.toResponse(any(Lesson.class))).thenReturn(response);

		var result = service.createLesson(request, courseId);
		assertEquals(lessonId, result.id());
	}

	@Test
	void shouldThrowWhenCourseNotFoundOnCreate() {
		var courseId = UUID.randomUUID();
		var request = new LessonCreateRequest(courseId, "Intro", 1);
		when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> service.createLesson(request, courseId));
		verify(lessonRepository, never()).save(any());
	}

	@Test
	void shouldGetLesson() {
		var id = UUID.randomUUID();
		var courseId = UUID.randomUUID();
		var lesson = Lesson.builder().id(id).title("Intro").orderIndex(1).build();
		var response = new LessonResponse(id, courseId, "Intro", 1);
		when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
		when(lessonMapper.toResponse(lesson)).thenReturn(response);

		var result = service.getLesson(id);
		assertEquals(id, result.id());
	}

	@Test
	void shouldUpdateLesson() {
		var id = UUID.randomUUID();
		var courseId = UUID.randomUUID();
		var request = new LessonUpdateRequest("Updated", 2);
		var lesson = Lesson.builder().id(id).title("Intro").orderIndex(1).build();
		var response = new LessonResponse(id, courseId, "Updated", 2);

		when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
		when(lessonRepository.save(lesson)).thenReturn(lesson);
		when(lessonMapper.toResponse(lesson)).thenReturn(response);

		var result = service.updateLesson(id, request);
		assertEquals("Updated", result.title());
	}

	@Test
	void shouldThrowWhenUpdateLessonNotFound() {
		var id = UUID.randomUUID();
		var request = new LessonUpdateRequest("Updated", 2);
		when(lessonRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> service.updateLesson(id, request));
		verify(lessonRepository, never()).save(any());
	}

	@Test
	void shouldDeleteLesson() {
		var id = UUID.randomUUID();
		var lesson = Lesson.builder().id(id).build();
		when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));

		service.delete(id);
		verify(lessonRepository).delete(lesson);
	}

	@Test
	void shouldFindByCourseId() {
		var courseId = UUID.randomUUID();
		var pageable = PageRequest.of(0, 10);
		var lesson = Lesson.builder().id(UUID.randomUUID()).title("Intro").orderIndex(1).build();
		var response = new LessonResponse(lesson.getId(), courseId, "Intro", 1);
		var page = new PageImpl<>(List.of(lesson), pageable, 1);

		when(lessonRepository.findByCourseId(courseId, pageable)).thenReturn(page);
		when(lessonMapper.toResponse(lesson)).thenReturn(response);

		var result = service.findByCourseId(courseId, pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void shouldFindByTitleContainingIgnoreCase() {
		var pageable = PageRequest.of(0, 10);
		var courseId = UUID.randomUUID();
		var lesson = Lesson.builder().id(UUID.randomUUID()).title("Spring Intro").orderIndex(1).build();
		var response = new LessonResponse(lesson.getId(), courseId, "Spring Intro", 1);
		var page = new PageImpl<>(List.of(lesson), pageable, 1);

		when(lessonRepository.findByTitleContainingIgnoreCase("spring", pageable)).thenReturn(page);
		when(lessonMapper.toResponse(lesson)).thenReturn(response);

		var result = service.findByTitleContainingIgnoreCase("spring", pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void shouldFindByCourseIdOrderByIndex() {
		var courseId = UUID.randomUUID();
		var pageable = PageRequest.of(0, 10);
		var lesson = Lesson.builder().id(UUID.randomUUID()).title("Intro").orderIndex(1).build();
		var response = new LessonResponse(lesson.getId(), courseId, "Intro", 1);
		var page = new PageImpl<>(List.of(lesson), pageable, 1);

		when(lessonRepository.findByCourseIdOrderByIndex(courseId, pageable)).thenReturn(page);
		when(lessonMapper.toResponse(lesson)).thenReturn(response);

		var result = service.findByCourseIdOrderByIndex(courseId, pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void shouldFindLessonsWithCourse() {
		var pageable = PageRequest.of(0, 10);
		var lesson = Lesson.builder().id(UUID.randomUUID()).title("Intro").orderIndex(1).build();
		var response = new LessonResponse(lesson.getId(), UUID.randomUUID(), "Intro", 1);
		var page = new PageImpl<>(List.of(lesson), pageable, 1);

		when(lessonRepository.findLessonsWithCourse(pageable)).thenReturn(page);
		when(lessonMapper.toResponse(lesson)).thenReturn(response);

		var result = service.findLessonsWithCourse(pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}
}
