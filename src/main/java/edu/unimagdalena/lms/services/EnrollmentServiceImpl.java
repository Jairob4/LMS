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
public class EnrollmentServiceImpl implements EnrollmentService {

	private final EnrollmentRepository enrollmentRepository;
	private final EnrollmentMapper enrollmentMapper;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;

	@Override
	public EnrollmentResponse createEnrollment(EnrollmentCreateRequest request, UUID studentId, UUID courseId) {
		Enrollment enrollment = enrollmentMapper.toEntity(request);
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new NotFoundException("Curso no encontrado"));

		enrollment.setStudent(student);
		enrollment.setCourse(course);

		Enrollment saved = enrollmentRepository.save(enrollment);
		return enrollmentMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public EnrollmentResponse getEnrollment(UUID id) {
		Enrollment enrollment = enrollmentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Inscripcion no encontrada"));
		return enrollmentMapper.toResponse(enrollment);
	}

	@Override
	public EnrollmentResponse updateEnrollment(UUID id, EnrollmentUpdateRequest request) {
		Enrollment existing = enrollmentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Inscripcion no encontrada"));
		enrollmentMapper.patch(existing, request);
		Enrollment saved = enrollmentRepository.save(existing);
		return enrollmentMapper.toResponse(saved);
	}

	@Override
	public void delete(UUID id) {
		Enrollment enrollment = enrollmentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Inscripcion no encontrada"));
		enrollmentRepository.delete(enrollment);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EnrollmentResponse> findByEnrolledAtAfter(Instant date, Pageable pageable) {
		return enrollmentRepository.findByEnrolledAtAfter(date, pageable).map(enrollmentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EnrollmentResponse> findByStudentId(UUID studentId, Pageable pageable) {
		return enrollmentRepository.findByStudentId(studentId, pageable).map(enrollmentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EnrollmentResponse> findByCourseId(UUID courseId, Pageable pageable) {
		return enrollmentRepository.findByCourseId(courseId, pageable).map(enrollmentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EnrollmentResponse> findByStatus(String status, Pageable pageable) {
		return enrollmentRepository.findByStatus(status, pageable).map(enrollmentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EnrollmentResponse> findByStudentFullName(String fullName, Pageable pageable) {
		return enrollmentRepository.findByStudentFullName(fullName, pageable).map(enrollmentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EnrollmentResponse> findByCourseTitle(String title, Pageable pageable) {
		return enrollmentRepository.findByCourseTitle(title, pageable).map(enrollmentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EnrollmentResponse> findByEnrolledAtBetween(Instant start, Instant end, Pageable pageable) {
		return enrollmentRepository.findByEnrolledAtBetween(start, end, pageable).map(enrollmentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<CourseEnrollmentStatsResponse> countEnrollmentsByCourse(Pageable pageable) {
		return enrollmentRepository.countEnrollmentsByCourse(pageable)
				.map(row -> new CourseEnrollmentStatsResponse(
						(UUID) row[0],
						(String) row[1],
						((Number) row[2]).longValue()
				));
	}
}
