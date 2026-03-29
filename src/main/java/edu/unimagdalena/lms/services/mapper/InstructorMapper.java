package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorCreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorResponse;
import edu.unimagdalena.lms.api.dto.InstructorDtos.InstructorUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Instructor;
import edu.unimagdalena.lms.domine.entities.InstructorProfile;
import java.util.HashSet;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface InstructorMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "courses", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	Instructor toEntity(InstructorCreateRequest req);

	InstructorResponse toResponse(Instructor entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "courses", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	void patch(@MappingTarget Instructor target, InstructorUpdateRequest changes);

	// Instructor no contiene profile; la relacion la mantiene InstructorProfile.
	default void bindProfile(Instructor instructor, InstructorProfile profile) {
		if (instructor != null && profile != null) {
			profile.setInstructor(instructor);
		}
	}

	@AfterMapping
	default void ensureCoursesInitialized(@MappingTarget Instructor target) {
		if (target.getCourses() == null) {
			target.setCourses(new HashSet<>());
		}
	}
}
