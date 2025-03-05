package ua.shpp.feniuk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ua.shpp.feniuk.Status;
import ua.shpp.feniuk.TaskMapper;
import ua.shpp.feniuk.dto.CreateTaskDTO;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.exeptions.StatusValidationException;
import ua.shpp.feniuk.exeptions.EntityNotFoundException;
import ua.shpp.feniuk.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository repository;
    private final MessageSource messageSource;
    private final TaskMapper taskMapper;

    public List<TaskDTO> getAllTasks() {
        log.info(resolveMessage("task.fetching.all"));
        List<TaskEntity> taskEntities = repository.findAll();

        return taskEntities.stream().map(taskMapper::toDto).toList();
    }

    public TaskDTO createTask(CreateTaskDTO createTaskDTO) {
        log.info(resolveMessage("task.created"));
        TaskEntity taskEntity = taskMapper.toEntity(createTaskDTO);

        taskEntity.setCreatedAt(LocalDate.now());
        taskEntity.setStatus(Status.PLANNED);

        return taskMapper.toDtoForPOST(repository.save(taskEntity));
    }

    public TaskDTO getTaskById(Long id) {
        log.info(resolveMessage("task.found", id));
        TaskEntity taskEntity = getTaskSpecifiedId(id);

        return taskMapper.toDto(taskEntity);
    }


    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        log.info(resolveMessage("task.updated", id));
        TaskEntity task = getTaskSpecifiedId(id);

        validateStatusTransition(task.getStatus(), taskDTO.getStatus());

        taskMapper.updateEntityFromDto(taskDTO, task);

        return taskMapper.toDto(repository.save(task));
    }

    public TaskDTO partiallyUpdateTask(Long id, TaskDTO taskDTO) {
        log.info(resolveMessage("task.partially.updated", id));
        TaskEntity task = getTaskSpecifiedId(id);

        validateStatusTransition(task.getStatus(), taskDTO.getStatus());

        taskMapper.partialUpdateEntityFromDto(taskDTO, task);

        return taskMapper.toDto(repository.save(task));
    }

    public void deleteTask(Long id) {
        log.info(resolveMessage("task.deleted", id));

        TaskEntity task = getTaskSpecifiedId(id);

        repository.delete(task);
    }

    private TaskEntity getTaskSpecifiedId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("task.notFound", id));
    }

    private void validateStatusTransition(Status currentStatus, Status newStatus) {
        if (newStatus != null && !currentStatus.isTransitionValid(newStatus)) {
            throw new StatusValidationException(
                    "error.invalid.status.transition",
                    currentStatus,
                    newStatus
            );
        }
    }

    private String resolveMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, "Default message",
                LocaleContextHolder.getLocale()
        );
    }
}
