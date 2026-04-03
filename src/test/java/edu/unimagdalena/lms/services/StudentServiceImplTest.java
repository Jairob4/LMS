package edu.unimagdalena.lms.services;

import edu.unimagdalena.lms.api.dto.StudentDtos.StudentCreateRequest;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentResponse;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Student;
import edu.unimagdalena.lms.domine.repositories.StudentRepository;
import edu.unimagdalena.lms.exception.NotFoundException;
import edu.unimagdalena.lms.services.mapper.StudentMapper;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

	@Mock
	private StudentRepository studentRepository;
	@Mock
	private StudentMapper studentMapper;

	@InjectMocks
	private StudentServiceImpl service;

	@Test
	void shouldCreateStudent() {
		var id = UUID.randomUUID();
		var request = new StudentCreateRequest("john@test.com", "John Doe");
		var entity = Student.builder().fullName("John Doe").email("john@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new StudentResponse(id, "john@test.com", "John Doe", Set.of(), Set.of(), Instant.now(), Instant.now());

		when(studentMapper.toEntity(request)).thenReturn(entity);
		when(studentRepository.save(entity)).thenAnswer(i -> {
			Student e = i.getArgument(0);
			e.setId(id);
			return e;
		});
		when(studentMapper.toResponse(any(Student.class))).thenReturn(response);

		var result = service.createStudent(request);
		assertEquals(id, result.id());
	}

	@Test
	void shouldGetStudentById() {
		var id = UUID.randomUUID();
		var entity = Student.builder().id(id).fullName("John Doe").email("john@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new StudentResponse(id, "john@test.com", "John Doe", Set.of(), Set.of(), entity.getCreatedAt(), entity.getUpdatedAt());

		when(studentRepository.findById(id)).thenReturn(Optional.of(entity));
		when(studentMapper.toResponse(entity)).thenReturn(response);

		var result = service.getStudent(id);
		assertEquals(id, result.id());
	}

	@Test
	void shouldThrowWhenStudentNotFoundById() {
		var id = UUID.randomUUID();
		when(studentRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> service.getStudent(id));
	}

	@Test
	void shouldGetStudentByEmail() {
		var id = UUID.randomUUID();
		var entity = Student.builder().id(id).fullName("John Doe").email("john@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new StudentResponse(id, "john@test.com", "John Doe", Set.of(), Set.of(), entity.getCreatedAt(), entity.getUpdatedAt());

		when(studentRepository.findByEmail("john@test.com")).thenReturn(Optional.of(entity));
		when(studentMapper.toResponse(entity)).thenReturn(response);

		var result = service.getStudentByEmail("john@test.com");
		assertEquals("john@test.com", result.email());
	}

	@Test
	void shouldThrowWhenStudentNotFoundByEmail() {
		when(studentRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> service.getStudentByEmail("missing@test.com"));
	}

	@Test
	void shouldUpdateStudent() {
		var id = UUID.randomUUID();
		var request = new StudentUpdateRequest("updated@test.com", "Updated");
		var entity = Student.builder().id(id).fullName("John Doe").email("john@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new StudentResponse(id, "updated@test.com", "Updated", Set.of(), Set.of(), entity.getCreatedAt(), Instant.now());

		when(studentRepository.findById(id)).thenReturn(Optional.of(entity));
		when(studentRepository.save(entity)).thenReturn(entity);
		when(studentMapper.toResponse(entity)).thenReturn(response);

		var result = service.updateStudent(id, request);
		assertEquals("Updated", result.fullName());
		assertEquals("updated@test.com", result.email());
	}

	@Test
	void shouldDeleteStudent() {
		var id = UUID.randomUUID();
		var entity = Student.builder().id(id).createdAt(Instant.now()).updatedAt(Instant.now()).build();
		when(studentRepository.findById(id)).thenReturn(Optional.of(entity));

		service.delete(id);
		verify(studentRepository).delete(entity);
	}

	@Test
	void shouldFindByFullNameContaining() {
		var pageable = PageRequest.of(0, 10);
		var entity = Student.builder().id(UUID.randomUUID()).fullName("John Doe").email("john@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new StudentResponse(entity.getId(), "john@test.com", "John Doe", Set.of(), Set.of(), entity.getCreatedAt(), entity.getUpdatedAt());
		var page = new PageImpl<>(List.of(entity), pageable, 1);

		when(studentRepository.findByFullNameContaining("John Doe", pageable)).thenReturn(page);
		when(studentMapper.toResponse(entity)).thenReturn(response);

		var result = service.findByFullNameContaining("John Doe", pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void shouldFindStudentsWithEnrollmentStatus() {
		var pageable = PageRequest.of(0, 10);
		var entity = Student.builder().id(UUID.randomUUID()).fullName("John Doe").email("john@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new StudentResponse(entity.getId(), "john@test.com", "John Doe", Set.of(), Set.of(), entity.getCreatedAt(), entity.getUpdatedAt());
		var page = new PageImpl<>(List.of(entity), pageable, 1);

		when(studentRepository.findStudentsWithEnrollmentStatus("ACTIVE", pageable)).thenReturn(page);
		when(studentMapper.toResponse(entity)).thenReturn(response);

		var result = service.findStudentsWithEnrollmentStatus("ACTIVE", pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}

	@Test
	void shouldFindStudentsWithAssessments() {
		var pageable = PageRequest.of(0, 10);
		var entity = Student.builder().id(UUID.randomUUID()).fullName("John Doe").email("john@test.com").createdAt(Instant.now()).updatedAt(Instant.now()).build();
		var response = new StudentResponse(entity.getId(), "john@test.com", "John Doe", Set.of(), Set.of(), entity.getCreatedAt(), entity.getUpdatedAt());
		var page = new PageImpl<>(List.of(entity), pageable, 1);

		when(studentRepository.findStudentsWithAssessments(pageable)).thenReturn(page);
		when(studentMapper.toResponse(entity)).thenReturn(response);

		var result = service.findStudentsWithAssessments(pageable);
		assertThat(result.getTotalElements()).isEqualTo(1);
	}
}
