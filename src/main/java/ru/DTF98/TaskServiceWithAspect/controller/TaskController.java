package ru.DTF98.TaskServiceWithAspect.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.DTF98.TaskServiceWithAspect.aspect.annotations.LoggerAnnotationBefore;
import ru.DTF98.TaskServiceWithAspect.dto.TaskRequestDto;
import ru.DTF98.TaskServiceWithAspect.dto.TaskResponseDto;
import ru.DTF98.TaskServiceWithAspect.model.Task;
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
    @LoggerAnnotationBefore
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    @LoggerAnnotationBefore
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDto updateTask(@PathVariable Long id, @RequestBody TaskRequestDto task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @LoggerAnnotationBefore
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    @LoggerAnnotationBefore
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDto> getAllTasks() {
        return taskService.getAllTasks();
    }
}
