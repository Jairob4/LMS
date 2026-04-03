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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

	private final CourseRepository courseRepository;
	private final InstructorRepository instructorRepository;
	private final CourseMapper courseMapper;

	@Override
	public CourseResponse createCourse(CourseCreateRequest request, UUID instructorId) {
		Course course = courseMapper.toEntity(request);
		Instructor instructor = instructorRepository.findById(instructorId)
				.orElseThrow(() -> new NotFoundException("Instructor no encontrado"));

		Instant now = Instant.now();
		course.setInstructor(instructor);
		course.setCreatedAt(now);
		course.setUpdatedAt(now);

		Course saved = courseRepository.save(course);
		return courseMapper.toResponse(saved);
	}

	@Transactional(readOnly = true)
	@Override
	public CourseResponse getCourse(UUID id) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));
		return courseMapper.toResponse(course);
	}

	@Override
	public CourseResponse updateCourse(UUID id, CourseUpdateRequest request) {
		Course existing = courseRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));

		courseMapper.patch(existing, request);
		existing.setUpdatedAt(Instant.now());

		Course saved = courseRepository.save(existing);
		return courseMapper.toResponse(saved);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> list(Pageable pageable) {
		return courseRepository.findAll(pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findByInstructorId(UUID instructorId, Pageable pageable) {
		return courseRepository.findByInstructorId(instructorId, pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findByStatus(String status, Pageable pageable) {
		return courseRepository.findByStatus(status, pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findByActive(boolean active, Pageable pageable) {
		return courseRepository.findByActive(active, pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findByTitleContainingIgnoreCase(String keyword, Pageable pageable) {
		return courseRepository.findByTitleContainingIgnoreCase(keyword, pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findByInstructorIdAndActive(UUID instructorId, boolean active, Pageable pageable) {
		return courseRepository.findByInstructorIdAndActive(instructorId, active, pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findActiveCoursesByInstructor(UUID instructorId, Pageable pageable) {
		return courseRepository.findActiveCoursesByInstructor(instructorId, pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findCoursesWithLessons(Pageable pageable) {
		return courseRepository.findCoursesWithLessons(pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findCoursesWithActiveEnrollments(Pageable pageable) {
		return courseRepository.findCoursesWithActiveEnrollments(pageable).map(courseMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CourseResponse> findCoursesWithAssessmentsAboveScore(double score, Pageable pageable) {
		return courseRepository.findCoursesWithAssessmentsAboveScore(score, pageable).map(courseMapper::toResponse);
	}

	@Override
	public void activateCourse(UUID id) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));
		course.setActive(true);
		course.setUpdatedAt(Instant.now());
		courseRepository.save(course);
	}

	@Override
	public void desactivateCourse(UUID id) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));
		course.setActive(false);
		course.setUpdatedAt(Instant.now());
		courseRepository.save(course);
	}

	@Override
	public void delete(UUID id) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));
		courseRepository.delete(course);
	}

	@Transactional(readOnly = true)
	@Override
	public boolean isCourseActive(UUID id) {
		Course course = courseRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));
		return course.isActive();
	}
}
