package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.StudentDtos.StudentCreateRequest;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentResponse;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Student;
import edu.unimagdalena.lms.domine.repositories.StudentRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.StudentMapper;
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
public class StudentServiceImpl implements StudentService {

	private final StudentMapper studentMapper;
	private final StudentRepository studentRepository;

	@Override
	public StudentResponse createStudent(StudentCreateRequest request) {
		Student student = studentMapper.toEntity(request);
		Instant now = Instant.now();
		student.setCreatedAt(now);
		student.setUpdatedAt(now);

		Student saved = studentRepository.save(student);
		return studentMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public StudentResponse getStudent(UUID id) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
		return studentMapper.toResponse(student);
	}

	@Override
	@Transactional(readOnly = true)
	public StudentResponse getStudentByEmail(String email) {
		Student student = studentRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
		return studentMapper.toResponse(student);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StudentResponse> list(Pageable pageable) {
		return studentRepository.findAll(pageable).map(studentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StudentResponse> findByFullNameContaining(String name, Pageable pageable) {
		return studentRepository.findByFullNameContaining(name, pageable).map(studentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StudentResponse> findStudentsWithEnrollmentStatus(String status, Pageable pageable) {
		return studentRepository.findStudentsWithEnrollmentStatus(status, pageable).map(studentMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<StudentResponse> findStudentsWithAssessments(Pageable pageable) {
		return studentRepository.findStudentsWithAssessments(pageable).map(studentMapper::toResponse);
	}

	@Override
	public StudentResponse updateStudent(UUID id, StudentUpdateRequest request) {
		Student existing = studentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

		studentMapper.patch(existing, request);
		existing.setUpdatedAt(Instant.now());

		Student saved = studentRepository.save(existing);
		return studentMapper.toResponse(saved);
	}

	@Override
	public void delete(UUID id) {
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));
		studentRepository.delete(student);
	}
}
