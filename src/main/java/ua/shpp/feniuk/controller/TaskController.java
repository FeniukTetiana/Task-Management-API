package ua.shpp.feniuk.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.service.TaskService;

import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskDTO taskDTO, Locale locale) {
        TaskEntity createdTask = taskService.createTask(taskDTO, locale);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<TaskEntity>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Long id, Locale locale) {
        return ResponseEntity.ok(taskService.getTaskById(id, locale));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO,
                                                 Locale locale) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO, locale));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<TaskEntity> partiallyUpdateTask(@PathVariable Long id,
                                                          @RequestBody TaskDTO taskDTO, Locale locale) {
        return ResponseEntity.ok(taskService.partiallyUpdateTask(id, taskDTO, locale));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> deleteTask(@PathVariable Long id, Locale locale) {
        taskService.deleteTask(id, locale);
        return ResponseEntity.ok(taskService.getMessage("task.deleted", new Object[]{id}, locale));
    }
}
