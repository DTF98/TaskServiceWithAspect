package ru.DTF98.TaskServiceWithAspect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.DTF98.TaskServiceWithAspect.exception.NotFoundException;
import ru.DTF98.TaskServiceWithAspect.kafka.producer.KafkaTaskProducer;
import ru.DTF98.TaskServiceWithAspect.model.Task;
import ru.DTF98.TaskServiceWithAspect.repository.TaskRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final KafkaTaskProducer kafkaTaskProducer;
    private final Environment env;
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
        Task response = taskRepository.save(existingTask);
        kafkaTaskProducer.sendTo(env.getProperty("kafka.topic.task-updates"), response);
        return response;
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