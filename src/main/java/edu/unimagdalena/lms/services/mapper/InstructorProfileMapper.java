package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileCreateRequest;
import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileResponse;
import edu.unimagdalena.lms.api.dto.InstructorProfileDtos.InstructorProfileUpdateRequest;
import edu.unimagdalena.lms.domine.entities.InstructorProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface InstructorProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "phone", source = "phoneNumber")
    InstructorProfile toEntity(InstructorProfileCreateRequest request);

    @Mapping(target = "instructorId", source = "instructor.id")
    @Mapping(target = "phoneNumber", source = "phone")
    InstructorProfileResponse toResponse(InstructorProfile entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instructor", ignore = true)
    @Mapping(target = "phone", source = "phoneNumber")
    void patch(@MappingTarget InstructorProfile target, InstructorProfileUpdateRequest changes);
}