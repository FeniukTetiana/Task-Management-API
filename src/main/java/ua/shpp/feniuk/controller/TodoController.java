package ua.shpp.feniuk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.shpp.feniuk.dto.TaskDTO;
import ua.shpp.feniuk.entity.TaskEntity;
import ua.shpp.feniuk.service.TaskService;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TodoController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskDTO taskDTO, Locale locale) {
        TaskEntity createdTask = taskService.createTask(taskDTO, locale);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskEntity>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Integer id, Locale locale) {
        return ResponseEntity.ok(taskService.getTaskById(Long.valueOf(id), locale));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable Integer id, @RequestBody TaskDTO taskDTO,
                                                 Locale locale) {
        return ResponseEntity.ok(taskService.updateTask(Long.valueOf(id), taskDTO, locale));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskEntity> partiallyUpdateTask(@PathVariable Integer id,
                                                          @RequestBody TaskDTO taskDTO, Locale locale) {
        return ResponseEntity.ok(taskService.partiallyUpdateTask(Long.valueOf(id), taskDTO, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id, Locale locale) {
        taskService.deleteTask(Long.valueOf(id), locale);
        return ResponseEntity.ok(taskService.getMessage("task.deleted", new Object[]{id}, locale));
    }
}
