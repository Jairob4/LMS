package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorCreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorResponse;
import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Instructor;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.InstructorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorServiceImpl implements InstructorService {

	private final InstructorRepository instructorRepository;
	private final InstructorMapper instructorMapper;

	@Override
	public InstructorResponse create(InstructorCreateRequest request) {
		Instructor instructor = instructorMapper.toEntity(request);
		Instant now = Instant.now();
		instructor.setCreatedAt(now);
		instructor.setUpdatedAt(now);
		Instructor saved = instructorRepository.save(instructor);
		return instructorMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public InstructorResponse get(UUID id) {
		Instructor instructor = instructorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Instructor no encontrado"));
		return instructorMapper.toResponse(instructor);
	}

	@Override
	@Transactional(readOnly = true)
	public InstructorResponse getByEmail(String email) {
		Instructor instructor = instructorRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Instructor no encontrado"));
		return instructorMapper.toResponse(instructor);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<InstructorResponse> list(Pageable pageable) {
		return instructorRepository.findAll(pageable).map(instructorMapper::toResponse);
	}

	@Override
	public InstructorResponse update(UUID id, InstructorUpdateRequest request) {
		Instructor existing = instructorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Instructor no encontrado"));

		instructorMapper.patch(existing, request);
		existing.setUpdatedAt(Instant.now());

		Instructor saved = instructorRepository.save(existing);
		return instructorMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<InstructorResponse> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable) {
		return instructorRepository.findByFullNameContainingIgnoreCase(fullName, pageable)
				.map(instructorMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<InstructorResponse> findByEmail(String email) {
		return instructorRepository.findByEmail(email).map(instructorMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return instructorRepository.existsByEmail(email);
	}

	@Override
	public void delete(UUID id) {
		Instructor instructor = instructorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Instructor no encontrado"));
		instructorRepository.delete(instructor);
	}
}
