package edu.unimagdalena.lms.services.mapper;

import edu.unimagdalena.lms.api.dto.LessonDtos.LessonCreateRequest;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonResponse;
import edu.unimagdalena.lms.api.dto.LessonDtos.LessonUpdateRequest;
import edu.unimagdalena.lms.domine.entities.Lesson;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LessonMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course.id", source = "courseId")
	Lesson toEntity(LessonCreateRequest req);

	@Mapping(target = "courseId", source = "course.id")
	LessonResponse toResponse(Lesson entity);

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "course", ignore = true)
	void patch(@MappingTarget Lesson target, LessonUpdateRequest changes);
}
