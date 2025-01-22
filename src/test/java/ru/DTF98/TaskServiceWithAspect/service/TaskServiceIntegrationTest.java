package ru.DTF98.TaskServiceWithAspect.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.DTF98.TaskServiceWithAspect.dto.TaskRequestDto;
import ru.DTF98.TaskServiceWithAspect.dto.TaskResponseDto;
import ru.DTF98.TaskServiceWithAspect.exception.NotFoundException;
import ru.DTF98.TaskServiceWithAspect.kafka.producer.KafkaTaskProducer;
import ru.DTF98.TaskServiceWithAspect.model.Task;
import ru.DTF98.TaskServiceWithAspect.repository.TaskRepository;
import ru.DTF98.TaskServiceWithAspect.testContainer.FullIntegrationPropertyTestContainer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class TaskServiceIntegrationTest extends FullIntegrationPropertyTestContainer {
    @Autowired
    private TaskService taskService;
    @Autowired
    private KafkaTaskProducer kafkaTaskProducer;
    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .userId(1L)
                .build();
        taskRepository.save(task);
    }

    @Test
    @DisplayName("Создание новой задачи")
    public void shouldCreateTask() {
        TaskRequestDto requestDto = TaskRequestDto.builder()
                .title("New Task")
                .description("New Task Description")
                .userId(2L)
                .build();

        TaskResponseDto responseDto = taskService.createTask(requestDto);

        assertThat(responseDto.getTitle()).isEqualTo("New Task");
        assertThat(responseDto.getDescription()).isEqualTo("New Task Description");
        assertThat(responseDto.getUserId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Получение задачи")
    public void shouldGetTaskById() {
        TaskResponseDto responseDto = taskService.getTaskById(task.getId());

        assertThat(responseDto.getTitle()).isEqualTo(task.getTitle());
        assertThat(responseDto.getDescription()).isEqualTo(task.getDescription());
        assertThat(responseDto.getUserId()).isEqualTo(task.getUserId());
    }

    @Test
    @DisplayName("Получение задачи - не найдена задача")
    public void shouldThrowNotFoundExceptionWhenTaskNotFound() {
        Long nonExistentTaskId = 999L;

        assertThrows(NotFoundException.class, () -> taskService.getTaskById(nonExistentTaskId));
    }

    @Test
    @DisplayName("Обновление задачи")
    public void shouldUpdateTask() {
        TaskRequestDto updateRequestDto = TaskRequestDto.builder()
                .title("Updated Title")
                .description("Updated Description")
                .userId(2L)
                .build();

        TaskResponseDto updatedTask = taskService.updateTask(task.getId(), updateRequestDto);

        assertThat(updatedTask.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTask.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedTask.getUserId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Удаление задачи")
    public void shouldDeleteTask() {
        Long taskId = task.getId();

        taskService.deleteTask(taskId);

        Optional<Task> deletedTask = taskRepository.findById(taskId);
        assertThat(deletedTask).isEmpty();
    }

    @Test
    @DisplayName("Получение всех задач")
    public void shouldGetAllTasks() {
        TaskRequestDto requestDto1 = TaskRequestDto.builder()
                .title("Task 1")
                .description("Description 1")
                .userId(1L)
                .build();
        taskService.createTask(requestDto1);

        TaskRequestDto requestDto2 = TaskRequestDto.builder()
                .title("Task 2")
                .description("Description 2")
                .userId(2L)
                .build();
        taskService.createTask(requestDto2);

        List<TaskResponseDto> allTasks = taskService.getAllTasks();

        assertThat(allTasks).hasSize(3);
    }
}
