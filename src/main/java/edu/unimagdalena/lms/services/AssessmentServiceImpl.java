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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

	private final AssessmentRepository assessmentRepository;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;
	private final AssessmentMapper assessmentMapper;

	@Override
	public AssessmentResponse createAssessment(AssessmentCreateRequest request, UUID studentId, UUID courseId) {
		validateCreateRequest(request);
		requireNonNull(studentId, "studentId");
		requireNonNull(courseId, "courseId");

		Assessment assessment = assessmentMapper.toEntity(request);
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));

		assessment.setStudent(student);
		assessment.setCourse(course);

		Assessment saved = assessmentRepository.save(assessment);
		return assessmentMapper.toResponse(saved);
	}

	@Transactional(readOnly = true)
	@Override
	public AssessmentResponse getAssessment(UUID id) {
		requireNonNull(id, "id");
		Assessment assessment = assessmentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Evaluacion no encontrada"));
		return assessmentMapper.toResponse(assessment);
	}

	@Override
	public void delete(UUID id) {
		requireNonNull(id, "id");
		Assessment assessment = assessmentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Evaluacion no encontrada"));
		assessmentRepository.delete(assessment);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AssessmentResponse> findByStudentFullName(String fullname, Pageable pageable) {
		requireNonBlank(fullname, "fullname");
		requireNonNull(pageable, "pageable");
		return assessmentRepository.findByStudentFullName(fullname, pageable).map(assessmentMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AssessmentResponse> findByCourseId(UUID courseId, Pageable pageable) {
		requireNonNull(courseId, "courseId");
		requireNonNull(pageable, "pageable");
		return assessmentRepository.findByCourseId(courseId, pageable).map(assessmentMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AssessmentResponse> findByType(String type, Pageable pageable) {
		requireNonBlank(type, "type");
		requireNonNull(pageable, "pageable");
		return assessmentRepository.findByType(type, pageable).map(assessmentMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AssessmentResponse> findByStudentIdAndScoreGreaterThanEqual(UUID studentId, int score, Pageable pageable) {
		requireNonNull(studentId, "studentId");
		requireNonNull(pageable, "pageable");
		if (score < 0) {
			throw new IllegalArgumentException("score must be non-negative");
		}
		return assessmentRepository.findByStudentIdAndScoreGreaterThanEqual(studentId, score, pageable)
				.map(assessmentMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AssessmentResponse> findByStudentIdAndType(UUID studentId, String type, Pageable pageable) {
		requireNonNull(studentId, "studentId");
		requireNonBlank(type, "type");
		requireNonNull(pageable, "pageable");
		return assessmentRepository.findByStudentIdAndType(studentId, type, pageable).map(assessmentMapper::toResponse);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<AssessmentResponse> findByTakenAtBetween(Instant startDate, Instant endDate, Pageable pageable) {
		requireNonNull(startDate, "startDate");
		requireNonNull(endDate, "endDate");
		requireNonNull(pageable, "pageable");
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("startDate must be before or equal to endDate");
		}
		return assessmentRepository.findByTakenAtBetween(startDate, endDate, pageable).map(assessmentMapper::toResponse);
	}

	private static void validateCreateRequest(AssessmentCreateRequest request) {
		requireNonNull(request, "request");
		requireNonBlank(request.type(), "type");
		if (request.score() < 0 || request.score() > 100) {
			throw new IllegalArgumentException("score must be between 0 and 100");
		}
		requireNonNull(request.takenAt(), "takenAt");
	}

	private static void requireNonBlank(String value, String field) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(field + " must not be blank");
		}
	}

	private static void requireNonNull(Object value, String field) {
		if (Objects.isNull(value)) {
			throw new IllegalArgumentException(field + " must not be null");
		}
	}
}
