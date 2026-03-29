package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.AssesmentDtos.AssesmentCreateRequest;
import edu.unimagdalena.lms.api.dto.AssesmentDtos.AssesmentUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Assesment;
import edu.unimagdalena.lms.api.dto.AssesmentDtos.AssesmentResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AssesmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student.id", source = "studentId")
    @Mapping(target = "course.id", source = "courseId")
    Assesment toEntity(AssesmentCreateRequest request);

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "courseId", source = "course.id")
    AssesmentResponse toResponse(Assesment assesment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    void patch(@MappingTarget Assesment target, AssesmentUpdateRequest changes);
}
