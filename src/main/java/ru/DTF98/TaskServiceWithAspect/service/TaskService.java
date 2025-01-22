package ru.DTF98.TaskServiceWithAspect.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.DTF98.SpringStarterLogging.annotations.LoggerAspectAfterReturning;
import ru.DTF98.SpringStarterLogging.annotations.LoggerAspectAfterThrowing;
import ru.DTF98.SpringStarterLogging.annotations.LoggerAspectAround;
import ru.DTF98.SpringStarterLogging.annotations.LoggerAspectBefore;
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
public class TaskService {
    private final TaskRepository taskRepository;
    private final KafkaTaskProducer kafkaTaskProducer;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, KafkaTaskProducer kafkaTaskProducer, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.kafkaTaskProducer = kafkaTaskProducer;
        this.taskMapper = taskMapper;
    }

    @LoggerAspectAround
    public TaskResponseDto createTask(TaskRequestDto requestDto) {
        Task task = taskMapper.toModelFromRequest(requestDto);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    @LoggerAspectAfterReturning
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long id) {
        return taskMapper.toResponse(taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Task with id = %d not found.", id))));
    }

    @LoggerAspectAfterReturning
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

    @LoggerAspectAfterThrowing
    public void deleteTask(Long id) {
        getTaskById(id);
        taskRepository.deleteById(id);
    }

    @LoggerAspectBefore
    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }
}