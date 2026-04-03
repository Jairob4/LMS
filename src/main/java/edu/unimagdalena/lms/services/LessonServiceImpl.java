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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {

	private final LessonMapper lessonMapper;
	private final LessonRepository lessonRepository;
	private final CourseRepository courseRepository;

	@Override
	public LessonResponse createLesson(LessonCreateRequest request, UUID courseId) {
		Lesson lesson = lessonMapper.toEntity(request);
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));

		lesson.setCourse(course);

		Lesson saved = lessonRepository.save(lesson);
		return lessonMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public LessonResponse getLesson(UUID id) {
		Lesson lesson = lessonRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Leccion no encontrada"));

		return lessonMapper.toResponse(lesson);
	}

	@Override
	public LessonResponse updateLesson(UUID id, LessonUpdateRequest request) {
		Lesson existing = lessonRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Leccion no encontrada"));

		lessonMapper.patch(existing, request);
		Lesson saved = lessonRepository.save(existing);

		return lessonMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LessonResponse> list(Pageable pageable) {
		return lessonRepository.findAll(pageable).map(lessonMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LessonResponse> findByCourseId(UUID courseId, Pageable pageable) {
		return lessonRepository.findByCourseId(courseId, pageable).map(lessonMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LessonResponse> findByTitleContainingIgnoreCase(String keyword, Pageable pageable) {
		return lessonRepository.findByTitleContainingIgnoreCase(keyword, pageable).map(lessonMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LessonResponse> findByCourseIdOrderByIndex(UUID courseId, Pageable pageable) {
		return lessonRepository.findByCourseIdOrderByIndex(courseId, pageable).map(lessonMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<LessonResponse> findLessonsWithCourse(Pageable pageable) {
		return lessonRepository.findLessonsWithCourse(pageable).map(lessonMapper::toResponse);
	}

	@Override
	public void delete(UUID id) {
		Lesson lesson = lessonRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Leccion no encontrada"));
		lessonRepository.delete(lesson);
	}
}
