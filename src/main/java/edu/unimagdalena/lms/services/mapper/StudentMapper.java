package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.StudentDtos.StudentCreateRequest;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentResponse;
import edu.unimagdalena.lms.api.dto.StudentDtos.StudentUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Student;
import java.util.HashSet;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface StudentMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enrollments", ignore = true)
	@Mapping(target = "assesments", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Student toEntity(StudentCreateRequest req);

	StudentResponse toResponse(Student entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "enrollments", ignore = true)
	@Mapping(target = "assesments", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void patch(@MappingTarget Student target, StudentUpdateRequest changes);

	@AfterMapping
	default void ensureCollectionsInitialized(@MappingTarget Student target) {
		if (target.getEnrollments() == null) {
			target.setEnrollments(new HashSet<>());
		}
		if (target.getAssesments() == null) {
			target.setAssesments(new HashSet<>());
		}
	}
}
