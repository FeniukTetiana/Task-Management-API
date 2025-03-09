package ua.shpp.feniuk.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ua.shpp.feniuk.Status;
import ua.shpp.feniuk.TaskMapper;
import ua.shpp.feniuk.dto.CreateTaskDTO;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.exeptions.EntityNotFoundException;
import ua.shpp.feniuk.exeptions.StatusValidationException;
import ua.shpp.feniuk.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private TaskDTO taskDTO;
    private TaskEntity taskEntity;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
        taskDTO.setStatus(Status.PLANNED);

        taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setStatus(Status.PLANNED);
    }

    @Test
    void testGetAllTasks() {
        when(repository.findAll()).thenReturn(List.of(taskEntity));
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDTO);
        when(messageSource.getMessage(eq("task.fetching.all"), any(), any(), any()))
                .thenReturn("Fetching all tasks");

        List<TaskDTO> result = taskService.getAllTasks();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void testCreateTask() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        TaskEntity newTaskEntity = new TaskEntity();
        newTaskEntity.setStatus(Status.PLANNED);

        when(taskMapper.toEntity(createTaskDTO)).thenReturn(newTaskEntity);
        when(repository.save(any(TaskEntity.class))).thenReturn(newTaskEntity);
        when(taskMapper.toDtoForPOST(newTaskEntity)).thenReturn(taskDTO);
        when(messageSource.getMessage(eq("task.created"), any(), any(), any())).thenReturn("Task created");

        TaskDTO result = taskService.createTask(createTaskDTO);

        assertNotNull(result);
        verify(repository).save(newTaskEntity);
    }

    @Test
    void testGetTaskById() {
        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDTO);
        when(messageSource.getMessage(eq("task.found"), any(), any(), any())).thenReturn("Task found");

        TaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(taskDTO, result);
    }

    @Test
    void testGetTaskById_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testUpdateTask_WhenValidStatusTransition() {
        TaskDTO updatedDTO = new TaskDTO();
        updatedDTO.setStatus(Status.WORK_IN_PROGRESS);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toDto(taskEntity)).thenReturn(updatedDTO);
        when(repository.save(taskEntity)).thenReturn(taskEntity);
        when(messageSource.getMessage(eq("task.updated"), any(), any(), any())).thenReturn("Task updated");

        TaskDTO result = taskService.updateTask(1L, updatedDTO);

        assertNotNull(result);
        verify(repository).save(taskEntity);
    }

    @Test
    void testUpdateTask_WhenInvalidStatusTransition() {
        TaskDTO updatedDTO = new TaskDTO();
        updatedDTO.setStatus(Status.DONE);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));

        assertThrows(StatusValidationException.class, () -> taskService.updateTask(1L, updatedDTO));
        verify(repository, never()).save(any());
    }

    @Test
    void testPartiallyUpdateTask_WhenValidStatus() {
        taskEntity.setStatus(Status.PLANNED);
        taskDTO.setStatus(Status.WORK_IN_PROGRESS);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));

        taskService.partiallyUpdateTask(1L, taskDTO);
    }

    @Test
    void testPartiallyUpdateTask_WhenInvalidStatus() {
        taskEntity.setStatus(Status.DONE);
        taskDTO.setStatus(Status.WORK_IN_PROGRESS);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));

        assertThrows(StatusValidationException.class, () -> taskService.partiallyUpdateTask(1L, taskDTO));
    }

    @Test
    void testPartiallyUpdateTask_WhenStatusNotChanged() {
        taskDTO.setStatus(null);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDTO);
        when(repository.save(taskEntity)).thenReturn(taskEntity);

        TaskDTO result = taskService.partiallyUpdateTask(1L, taskDTO);

        assertNotNull(result);
        assertEquals(Status.PLANNED, taskEntity.getStatus()); // Перевіряємо, що статус залишився незмінним
        verify(repository).save(taskEntity);
    }

    @Test
    void testDeleteTask_WhenValidId() {
        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(messageSource.getMessage(eq("task.deleted"), any(), any(), any())).thenReturn("Task deleted");

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(repository).delete(taskEntity);
    }

    @Test
    void testDeleteTask_WhenInvalidId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(1L));
    }
}