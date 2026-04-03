package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorCreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorResponse;
import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Instructor;
import edu.unimagdalena.lms.domine.repositories.InstructorRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.InstructorMapper;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceImplTest {

	@Mock
	private InstructorRepository instructorRepository;
	@Mock
	private InstructorMapper instructorMapper;

	@InjectMocks
	private InstructorServiceImpl service;

	@Test
	void shouldCreateInstructor() {
		var id = UUID.randomUUID();
		var request = new InstructorCreateRequest("jane@test.com", "Jane Doe");
		var now = Instant.now();
		var entity = Instructor.builder().fullName("Jane Doe").email("jane@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new InstructorResponse(id, "jane@test.com", "Jane Doe", now, now, Set.of());

		when(instructorMapper.toEntity(request)).thenReturn(entity);
		when(instructorRepository.save(entity)).thenAnswer(i -> {
			Instructor e = i.getArgument(0);
			e.setId(id);
			return e;
		});
		when(instructorMapper.toResponse(any(Instructor.class))).thenReturn(response);

		var result = service.create(request);
		assertEquals(id, result.id());
	}

	@Test
	void shouldGetInstructorById() {
		var id = UUID.randomUUID();
		var now = Instant.now();
		var entity = Instructor.builder().id(id).fullName("Jane Doe").email("jane@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new InstructorResponse(id, "jane@test.com", "Jane Doe", now, now, Set.of());

		when(instructorRepository.findById(id)).thenReturn(Optional.of(entity));
		when(instructorMapper.toResponse(entity)).thenReturn(response);

		var result = service.get(id);
		assertEquals(id, result.id());
	}

	@Test
	void shouldThrowWhenInstructorNotFoundById() {
		var id = UUID.randomUUID();
		when(instructorRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> service.get(id));
	}

	@Test
	void shouldGetInstructorByEmail() {
		var id = UUID.randomUUID();
		var now = Instant.now();
		var entity = Instructor.builder().id(id).fullName("Jane Doe").email("jane@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new InstructorResponse(id, "jane@test.com", "Jane Doe", now, now, Set.of());

		when(instructorRepository.findByEmail("jane@test.com")).thenReturn(Optional.of(entity));
		when(instructorMapper.toResponse(entity)).thenReturn(response);

		var result = service.getByEmail("jane@test.com");
		assertEquals("jane@test.com", result.email());
	}

	@Test
	void shouldThrowWhenInstructorNotFoundByEmail() {
		when(instructorRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> service.getByEmail("missing@test.com"));
	}

	@Test
	void shouldUpdateInstructor() {
		var id = UUID.randomUUID();
		var request = new InstructorUpdateRequest("updated@test.com", "Updated Name");
		var now = Instant.now();
		var entity = Instructor.builder().id(id).fullName("Jane Doe").email("jane@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new InstructorResponse(id, "updated@test.com", "Updated Name", now, now, Set.of());

		when(instructorRepository.findById(id)).thenReturn(Optional.of(entity));
		when(instructorRepository.save(entity)).thenReturn(entity);
		when(instructorMapper.toResponse(entity)).thenReturn(response);

		var result = service.update(id, request);
		assertEquals("Updated Name", result.fullName());
		assertEquals("updated@test.com", result.email());
	}

	@Test
	void shouldDeleteInstructor() {
		var id = UUID.randomUUID();
		var entity = Instructor.builder().id(id).createdAt(Instant.now()).updatedAt(Instant.now()).build();
		when(instructorRepository.findById(id)).thenReturn(Optional.of(entity));

		service.delete(id);
		verify(instructorRepository).delete(entity);
	}

	@Test
	void shouldFindByFullNameIgnoreCase() {
		var pageable = PageRequest.of(0, 10);
		var now = Instant.now();
		var entity = Instructor.builder().id(UUID.randomUUID()).fullName("Jane Doe").email("jane@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new InstructorResponse(entity.getId(), "jane@test.com", "Jane Doe", now, now, Set.of());
		var page = new PageImpl<>(List.of(entity), pageable, 1);

		when(instructorRepository.findByFullNameContainingIgnoreCase("Jane Doe", pageable)).thenReturn(page);
		when(instructorMapper.toResponse(entity)).thenReturn(response);

		var result = service.findByFullNameContainingIgnoreCase("Jane Doe", pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void shouldFindByEmail() {
		var now = Instant.now();
		var entity = Instructor.builder().id(UUID.randomUUID()).fullName("Jane Doe").email("jane@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new InstructorResponse(entity.getId(), "jane@test.com", "Jane Doe", now, now, Set.of());

		when(instructorRepository.findByEmail("jane@test.com")).thenReturn(Optional.of(entity));
		when(instructorMapper.toResponse(entity)).thenReturn(response);

		var result = service.findByEmail("jane@test.com");
		assertThat(result).isPresent();
	}

	@Test
	void shouldReturnEmptyWhenFindByEmailDoesNotExist() {
		when(instructorRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

		var result = service.findByEmail("missing@test.com");
		assertThat(result).isEmpty();
	}

	@Test
	void shouldCheckExistsByEmail() {
		when(instructorRepository.existsByEmail("jane@test.com")).thenReturn(true);

		var result = service.existsByEmail("jane@test.com");
		assertThat(result).isTrue();
		verify(instructorRepository, never()).findByEmail(any());
	}
}
