package ua.shpp.feniuk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ua.shpp.feniuk.Status;
import ua.shpp.feniuk.TaskMapper;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.exeptions.StatusValidationException;
import ua.shpp.feniuk.exeptions.EntityNotFoundException;
import ua.shpp.feniuk.repository.TaskRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository repository;
    private final MessageSource messageSource;
    private final TaskMapper taskMapper;

    public List<TaskEntity> getAllTasks() {
        log.info(resolveMessage("task.fetching.all"));
        return repository.findAll();
    }

    public TaskEntity createTask(TaskDTO taskDTO) {
        log.info(resolveMessage("task.created"));
        return repository.save(taskMapper.toEntity(taskDTO));
    }

    public TaskEntity getTaskById(Long id) {
        log.info(resolveMessage("task.found", id));
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("task.notFound", id));
    }

    public TaskEntity updateTask(Long id, TaskDTO taskDTO) {
        log.info(resolveMessage("task.updated", id));
        TaskEntity existingTask = getTaskById(id);

        validateStatusTransition(existingTask.getStatus(), taskDTO.getStatus());

        taskMapper.updateEntityFromDto(taskDTO, existingTask);
        return repository.save(existingTask);
    }

    public TaskEntity partiallyUpdateTask(Long id, TaskDTO taskDTO) {
        log.info(resolveMessage("task.partially.updated", id));
        TaskEntity existingTask = getTaskById(id);

        if (taskDTO.getStatus() != null) {
            validateStatusTransition(existingTask.getStatus(), taskDTO.getStatus());
        }

        taskMapper.partialUpdateEntityFromDto(taskDTO, existingTask);
        return repository.save(existingTask);
    }

    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("task.notFound", id);
        }
        log.info(resolveMessage("task.deleted", id));
        repository.deleteById(id);
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
