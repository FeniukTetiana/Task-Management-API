package ua.shpp.feniuk.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ua.shpp.feniuk.Status;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private TaskService taskService;

    private Locale locale;
    private TaskDTO taskDTO;
    private TaskEntity taskEntity;

    @BeforeEach
    void setUp() {
        locale = Locale.ENGLISH;

        taskDTO = TaskDTO.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .createdAt(LocalDate.now())
                .status(Status.PLANNED)
                .build();

        taskEntity = TaskEntity.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .createdAt(taskDTO.getCreatedAt())
                .status(Status.PLANNED)
                .build();
    }

    @Test
    void createTask_ShouldReturnSavedTask() {
        when(repository.save(any(TaskEntity.class))).thenReturn(taskEntity);

        TaskEntity result = taskService.createTask(taskDTO, locale);

        assertNotNull(result);
        assertEquals(taskEntity.getTitle(), result.getTitle());
        verify(repository).save(any(TaskEntity.class));
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() {
        List<TaskEntity> taskList = List.of(taskEntity);
        when(repository.findAll()).thenReturn(taskList);

        List<TaskEntity> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));

        TaskEntity result = taskService.getTaskById(1L, locale);

        assertNotNull(result);
        assertEquals(taskEntity.getId(), result.getId());
        verify(repository).findById(1L);
    }

    @Test
    void updateTask_ShouldUpdateAndReturnTask() {
        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(repository.save(any(TaskEntity.class))).thenReturn(taskEntity);

        TaskEntity result = taskService.updateTask(1L, taskDTO, locale);

        assertNotNull(result);
        verify(repository).save(any(TaskEntity.class));
    }

    @Test
    void updateTask_ShouldThrowException_WhenInvalidStatusTransition() {
        taskDTO.setStatus(Status.DONE); // Неприпустимий перехід з PLANNED → DONE

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> taskService.updateTask(1L, taskDTO, locale));

        assertTrue(exception.getMessage().contains("Invalid status transition"));
    }

    @Test
    void partiallyUpdateTask_ShouldUpdateStatusOnly() {
        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(repository.save(any(TaskEntity.class))).thenReturn(taskEntity);

        taskDTO.setStatus(Status.WORK_IN_PROGRESS);
        TaskEntity result = taskService.partiallyUpdateTask(1L, taskDTO, locale);

        assertEquals(Status.WORK_IN_PROGRESS, result.getStatus());
        verify(repository).save(any(TaskEntity.class));
    }

    @Test
    void deleteTask_ShouldCallRepositoryDeleteById() {
        taskService.deleteTask(1L, locale);

        verify(repository).deleteById(1L);
    }

    @Test
    void isTransitionValid_ShouldReturnTrue_ForValidTransitions() {
        assertTrue(Status.PLANNED.isTransitionValid(Status.PLANNED, Status.WORK_IN_PROGRESS));
        assertTrue(Status.WORK_IN_PROGRESS.isTransitionValid(Status.WORK_IN_PROGRESS, Status.NOTIFIED));
        assertTrue(Status.NOTIFIED.isTransitionValid(Status.NOTIFIED, Status.DONE));
        assertTrue(Status.PLANNED.isTransitionValid(Status.PLANNED, Status.POSTPONED));
        assertTrue(Status.WORK_IN_PROGRESS.isTransitionValid(Status.WORK_IN_PROGRESS, Status.SIGNED));
    }

    @Test
    void isTransitionValid_ShouldReturnFalse_ForInvalidTransitions() {
        assertFalse(Status.PLANNED.isTransitionValid(Status.PLANNED, Status.DONE));
        assertFalse(Status.SIGNED.isTransitionValid(Status.SIGNED, Status.PLANNED));
        assertFalse(Status.DONE.isTransitionValid(Status.DONE, Status.WORK_IN_PROGRESS));
    }

    @Test
    void isTransitionValid_ShouldAllowCancelAtAnyStage() {
        assertTrue(Status.PLANNED.isTransitionValid(Status.PLANNED, Status.CANCELLED));
        assertTrue(Status.WORK_IN_PROGRESS.isTransitionValid(Status.WORK_IN_PROGRESS, Status.CANCELLED));
        assertTrue(Status.NOTIFIED.isTransitionValid(Status.NOTIFIED, Status.CANCELLED));
    }
}
