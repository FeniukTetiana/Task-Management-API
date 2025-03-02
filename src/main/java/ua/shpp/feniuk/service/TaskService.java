package ua.shpp.feniuk.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.repository.TaskRepository;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository repository;
    private final MessageSource messageSource;

    public TaskEntity createTask(TaskDTO taskDTO, Locale locale) {
        log.info(messageSource.getMessage("task.created", null, locale));

        TaskEntity taskEntity = TaskEntity.builder()
                .id(taskDTO.getId())
                .description(taskDTO.getDescription())
                .createdAt(taskDTO.getCreatedAt())
                .status(taskDTO.getStatus())
                .build();
        return repository.save(taskEntity);
    }

    public List<TaskEntity> getAllTasks() {
        log.info("Fetching all tasks");
        return repository.findAll();
    }

    public TaskEntity getTaskById(Long id, Locale locale) {
        log.info(messageSource.getMessage("task.found", new Object[]{id}, locale));
        return repository.findById(Math.toIntExact(id)).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("task.notFound",
                        new Object[]{id}, locale)));
    }

    public TaskEntity updateTask(Long id, TaskDTO taskDTO, Locale locale) {
        log.info(messageSource.getMessage("task.updated", new Object[]{id}, locale));
        TaskEntity existingTask = getTaskById(id, locale);

        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setCreatedAt(taskDTO.getCreatedAt());
        existingTask.setStatus(taskDTO.getStatus());

        return repository.save(existingTask);
    }

    public TaskEntity partiallyUpdateTask(Long id, TaskDTO taskDTO, Locale locale) {
        log.info(messageSource.getMessage("task.updated", new Object[]{id}, locale));
        TaskEntity task = getTaskById(id, locale);

        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getCreatedAt() != null) {
            task.setCreatedAt(taskDTO.getCreatedAt());
        }
        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }

        return repository.save(task);
    }

    public void deleteTask(Long id, Locale locale) {
        log.info(messageSource.getMessage("task.deleted", new Object[]{id}, locale));
        repository.deleteById(Math.toIntExact(id));
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, locale);
    }
}
