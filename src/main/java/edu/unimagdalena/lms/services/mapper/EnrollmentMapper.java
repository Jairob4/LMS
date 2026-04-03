package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentCreateRequest;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentResponse;
import edu.unimagdalena.lms.api.dto.EnrollmentDtos.EnrollmentUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Enrollment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "student.id", source = "studentId")
	@Mapping(target = "course.id", source = "courseId")
	Enrollment toEntity(EnrollmentCreateRequest request);

	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "courseId", source = "course.id")
	EnrollmentResponse toResponse(Enrollment entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "course", ignore = true)
	void patch(@MappingTarget Enrollment target, EnrollmentUpdateRequest changes);
}
