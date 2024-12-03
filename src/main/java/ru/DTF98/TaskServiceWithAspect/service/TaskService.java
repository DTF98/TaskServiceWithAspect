package ru.DTF98.TaskServiceWithAspect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.DTF98.TaskServiceWithAspect.exception.NotFoundException;
import ru.DTF98.TaskServiceWithAspect.model.Task;
import ru.DTF98.TaskServiceWithAspect.repository.TaskRepository;

import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Task with id = %d not found.", id)));
    }

    public Task updateTask(Long id, Task task) {
        Task existingTask = getTaskById(id);
        if (task.getTitle() != null) {
            existingTask.setTitle(task.getTitle());
        } else if (task.getDescription() != null) {
            existingTask.setDescription(task.getDescription());
        } else if (task.getUserId() != null) {
            existingTask.setUserId(task.getUserId());
        }
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        getTaskById(id);
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}