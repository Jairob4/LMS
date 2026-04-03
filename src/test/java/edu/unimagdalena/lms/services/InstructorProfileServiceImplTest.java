package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileCreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileResponse;
import edu.unimagdalena.lms.domine.entities.Instructor;
import edu.unimagdalena.lms.domine.entities.InstructorProfile;
import edu.unimagdalena.lms.domine.repositories.InstructorProfileRepository;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.InstructorProfileMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InstructorProfileServiceImplTest {

	@Mock
	private InstructorRepository instructorRepository;
	@Mock
	private InstructorProfileRepository instructorProfileRepository;
	@Mock
	private InstructorProfileMapper instructorProfileMapper;

	@InjectMocks
	private InstructorProfileServiceImpl service;

	@Test
	void shouldCreateInstructorProfile() {
		var instructorId = UUID.randomUUID();
		var profileId = UUID.randomUUID();
		var request = new InstructorProfileCreateRequest("3001234567", "Bio");
		var instructor = Instructor.builder().id(instructorId).email("i@test.com").fullName("Instructor").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var profile = InstructorProfile.builder().phone("3001234567").bio("Bio").build();
		var response = new InstructorProfileResponse(profileId, "3001234567", "Bio", instructorId);

		when(instructorProfileMapper.toEntity(request)).thenReturn(profile);
		when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
		when(instructorProfileRepository.save(any(InstructorProfile.class))).thenAnswer(i -> {
			InstructorProfile p = i.getArgument(0);
			p.setId(profileId);
			return p;
		});
		when(instructorProfileMapper.toResponse(any(InstructorProfile.class))).thenReturn(response);

		var result = service.createInstructorProfile(request, instructorId);
		assertEquals(profileId, result.id());
	}

	@Test
	void shouldThrowWhenInstructorNotFoundOnCreate() {
		var instructorId = UUID.randomUUID();
		var request = new InstructorProfileCreateRequest("3001234567", "Bio");
		when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> service.createInstructorProfile(request, instructorId));
		verify(instructorProfileRepository, never()).save(any());
	}

	@Test
	void shouldGetInstructorProfile() {
		var id = UUID.randomUUID();
		var profile = InstructorProfile.builder().id(id).phone("3001234567").bio("Bio").build();
		var response = new InstructorProfileResponse(id, "3001234567", "Bio", UUID.randomUUID());
		when(instructorProfileRepository.findById(id)).thenReturn(Optional.of(profile));
		when(instructorProfileMapper.toResponse(profile)).thenReturn(response);

		var result = service.getInstructorProfile(id);
		assertEquals(id, result.id());
	}

	@Test
	void shouldDeleteInstructorProfile() {
		var id = UUID.randomUUID();
		var profile = InstructorProfile.builder().id(id).build();
		when(instructorProfileRepository.findById(id)).thenReturn(Optional.of(profile));

		service.delete(id);
		verify(instructorProfileRepository).delete(profile);
	}

	@Test
	void shouldFindByInstructorFullNameIgnoreCase() {
		var pageable = PageRequest.of(0, 10);
		var profile = InstructorProfile.builder().id(UUID.randomUUID()).phone("3001234567").bio("Bio").build();
		var response = new InstructorProfileResponse(profile.getId(), "3001234567", "Bio", UUID.randomUUID());
		var page = new PageImpl<>(List.of(profile), pageable, 1);

		when(instructorProfileRepository.findByInstructorFullNameIgnoreCase("John", pageable)).thenReturn(page);
		when(instructorProfileMapper.toResponse(profile)).thenReturn(response);

		var result = service.findByInstructorFullNameIgnoreCase("John", pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void shouldFindByPhoneNumber() {
		var profile = InstructorProfile.builder().id(UUID.randomUUID()).phone("3001234567").bio("Bio").build();
		var response = new InstructorProfileResponse(profile.getId(), "3001234567", "Bio", UUID.randomUUID());
		when(instructorProfileRepository.findByPhone("3001234567")).thenReturn(Optional.of(profile));
		when(instructorProfileMapper.toResponse(profile)).thenReturn(response);

		var result = service.findByPhoneNumber("3001234567");
		assertThat(result).isPresent();
	}
}
