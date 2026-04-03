package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileCreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileResponse;
import edu.unimagdalena.lms.domine.entities.InstructorProfile;
import edu.unimagdalena.lms.domine.repositories.InstructorProfileRepository;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.InstructorProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InstructorProfileServiceImpl implements InstructorProfileService {

	private final InstructorRepository instructorRepository;
	private final InstructorProfileRepository instructorProfileRepository;
	private final InstructorProfileMapper instructorProfileMapper;

	@Override
	public InstructorProfileResponse createInstructorProfile(InstructorProfileCreateRequest request, UUID instructorId) {
		InstructorProfile profile = instructorProfileMapper.toEntity(request);
		var instructor = instructorRepository.findById(instructorId)
				.orElseThrow(() -> new NotFoundException("Instructor no encontrado"));

		profile.setInstructor(instructor);

		InstructorProfile saved = instructorProfileRepository.save(profile);
		return instructorProfileMapper.toResponse(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public InstructorProfileResponse getInstructorProfile(UUID id) {
		InstructorProfile profile = instructorProfileRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Perfil de instructor no encontrado"));
		return instructorProfileMapper.toResponse(profile);
	}

	@Override
	public void delete(UUID id) {
		InstructorProfile profile = instructorProfileRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Perfil de instructor no encontrado"));
		instructorProfileRepository.delete(profile);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<InstructorProfileResponse> findByInstructorFullNameIgnoreCase(String fullName, Pageable pageable) {
		return instructorProfileRepository.findByInstructorFullNameIgnoreCase(fullName, pageable)
				.map(instructorProfileMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<InstructorProfileResponse> findByPhoneNumber(String phone) {
		return instructorProfileRepository.findByPhone(phone).map(instructorProfileMapper::toResponse);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByPhoneNumber(String phone) {
		return instructorProfileRepository.existsByPhone(phone);
	}
}
