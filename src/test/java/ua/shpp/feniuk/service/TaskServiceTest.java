package ua.shpp.feniuk.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ua.shpp.feniuk.Status;
import ua.shpp.feniuk.mapper.TaskMapper;
import ua.shpp.feniuk.dto.CreateTaskDTO;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.exeptions.EntityNotFoundException;
import ua.shpp.feniuk.exeptions.StatusValidationException;
import ua.shpp.feniuk.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
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

        when(messageSource.getMessage(anyString(), any(), anyString(), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testGetAllTasks() {
        when(repository.findAll()).thenReturn(List.of(taskEntity));
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.getAllTasks();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository).findAll();
        verify(taskMapper).toDto(taskEntity);
    }

    @Test
    void testCreateTask() {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO();
        TaskEntity newTaskEntity = new TaskEntity();
        newTaskEntity.setStatus(Status.PLANNED);

        when(taskMapper.toEntity(createTaskDTO)).thenReturn(newTaskEntity);
        when(repository.save(any(TaskEntity.class))).thenReturn(newTaskEntity);
        when(taskMapper.toDtoForPOST(newTaskEntity)).thenReturn(taskDTO);

        TaskDTO result = taskService.createTask(createTaskDTO);

        assertNotNull(result);

        ArgumentCaptor<TaskEntity> captor = ArgumentCaptor.forClass(TaskEntity.class);
        verify(repository).save(captor.capture());

        TaskEntity savedEntity = captor.getValue();
        assertEquals(Status.PLANNED, savedEntity.getStatus());
        assertEquals(LocalDate.now(), savedEntity.getCreatedAt());

        verify(taskMapper).toDtoForPOST(newTaskEntity);
    }

    @Test
    void testGetTaskById() {
        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDTO);

        TaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals(taskDTO, result);
        verify(taskMapper).toDto(taskEntity);
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

        taskEntity.setStatus(Status.PLANNED);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(repository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDto(taskEntity)).thenReturn(updatedDTO);

        TaskDTO result = taskService.updateTask(1L, updatedDTO);

        assertNotNull(result);
        verify(taskMapper).updateEntityFromDto(updatedDTO, taskEntity);
        verify(repository).save(taskEntity);
        verify(taskMapper).toDto(taskEntity);
    }

    @Test
    void testUpdateTask_WhenInvalidStatusTransition() {
        TaskDTO updatedDTO = new TaskDTO();
        updatedDTO.setStatus(Status.DONE);

        taskEntity.setStatus(Status.PLANNED);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));

        assertThrows(StatusValidationException.class, () -> taskService.updateTask(1L, updatedDTO));
        verify(repository, never()).save(any());
        verify(taskMapper, never()).updateEntityFromDto(any(), any());
    }

    @Test
    void testPartiallyUpdateTask_WhenValidStatus() {
        taskEntity.setStatus(Status.PLANNED);
        taskDTO.setStatus(Status.WORK_IN_PROGRESS);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(repository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDTO);

        TaskDTO result = taskService.partiallyUpdateTask(1L, taskDTO);

        assertNotNull(result);
        verify(taskMapper).partialUpdateEntityFromDto(taskDTO, taskEntity);
        verify(repository).save(taskEntity);
        verify(taskMapper).toDto(taskEntity);
    }

    @Test
    void testPartiallyUpdateTask_WhenInvalidStatus() {
        taskEntity.setStatus(Status.DONE);
        taskDTO.setStatus(Status.WORK_IN_PROGRESS);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));

        assertThrows(StatusValidationException.class, () -> taskService.partiallyUpdateTask(1L, taskDTO));
        verify(repository, never()).save(any());
        verify(taskMapper, never()).partialUpdateEntityFromDto(any(), any());
    }

    @Test
    void testPartiallyUpdateTask_WhenStatusNotChanged() {
        taskDTO.setStatus(null);
        taskEntity.setStatus(Status.PLANNED);

        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(repository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDTO);

        TaskDTO result = taskService.partiallyUpdateTask(1L, taskDTO);

        assertNotNull(result);
        assertEquals(Status.PLANNED, taskEntity.getStatus());
        verify(repository).save(taskEntity);
        verify(taskMapper).partialUpdateEntityFromDto(taskDTO, taskEntity);
    }

    @Test
    void testDeleteTask_WhenValidId() {
        when(repository.findById(1L)).thenReturn(Optional.of(taskEntity));

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(repository).delete(taskEntity);
    }

    @Test
    void testDeleteTask_WhenInvalidId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(1L));
        verify(repository, never()).delete(any());
    }
}