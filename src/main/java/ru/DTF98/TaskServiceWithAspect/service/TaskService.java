package ru.DTF98.TaskServiceWithAspect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.DTF98.TaskServiceWithAspect.dto.TaskRequestDto;
import ru.DTF98.TaskServiceWithAspect.dto.TaskResponseDto;
import ru.DTF98.TaskServiceWithAspect.exception.NotFoundException;
import ru.DTF98.TaskServiceWithAspect.kafka.producer.KafkaTaskProducer;
import ru.DTF98.TaskServiceWithAspect.mapper.TaskMapper;
import ru.DTF98.TaskServiceWithAspect.model.Task;
import ru.DTF98.TaskServiceWithAspect.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.DTF98.TaskServiceWithAspect.config.KafkaConfig.KafkaConfigConstants.DEFAULT_TOPIC;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final KafkaTaskProducer kafkaTaskProducer;
    private final TaskMapper taskMapper;

    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        Task task = taskMapper.toModelFromRequest(requestDto);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long id) {
        return taskMapper.toResponse(taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Task with id = %d not found.", id))));
    }

    public TaskResponseDto updateTask(Long id, TaskRequestDto requestDto) {
        Task existingTask = taskMapper.toModelFromResponse(getTaskById(id));
        if (requestDto.getTitle() != null) {
            existingTask.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null) {
            existingTask.setDescription(requestDto.getDescription());
        }
        if (requestDto.getUserId() != null) {
            existingTask.setUserId(requestDto.getUserId());
        }
        Task response = taskRepository.save(existingTask);
        kafkaTaskProducer.sendTo(DEFAULT_TOPIC, response);
        return taskMapper.toResponse(response);
    }

    public void deleteTask(Long id) {
        getTaskById(id);
        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }
}