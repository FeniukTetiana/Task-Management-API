package ua.shpp.feniuk;

import org.mapstruct.*;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    TaskEntity toEntity(TaskDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(TaskDTO dto, @MappingTarget TaskEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void partialUpdateEntityFromDto(TaskDTO dto, @MappingTarget TaskEntity entity);
}
