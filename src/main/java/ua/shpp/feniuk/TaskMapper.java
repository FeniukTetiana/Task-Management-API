package ua.shpp.feniuk;

import org.mapstruct.*;
import ua.shpp.feniuk.dto.CreateTaskDTO;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface TaskMapper {

    @Mapping(target = "id", source = "id")
    TaskDTO toDto(TaskEntity entity);

    @Mapping(target = "id", ignore = true)
    TaskEntity toEntity(CreateTaskDTO createTaskDTO);

    @Mapping(target = "id", source = "id")
    TaskDTO toDtoForPOST(TaskEntity taskEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(TaskDTO dto, @MappingTarget TaskEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void partialUpdateEntityFromDto(TaskDTO dto, @MappingTarget TaskEntity entity);
}
