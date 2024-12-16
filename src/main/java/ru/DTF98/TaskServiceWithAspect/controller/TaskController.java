package ru.DTF98.TaskServiceWithAspect.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.DTF98.TaskServiceWithAspect.dto.TaskRequestDto;
import ru.DTF98.TaskServiceWithAspect.dto.TaskResponseDto;
import ru.DTF98.TaskServiceWithAspect.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Validated
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@Valid @RequestBody TaskRequestDto task) {
        return taskService.createTask(task);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto updateTask(@PathVariable Long id, @RequestBody TaskRequestDto task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks();
    }
}
