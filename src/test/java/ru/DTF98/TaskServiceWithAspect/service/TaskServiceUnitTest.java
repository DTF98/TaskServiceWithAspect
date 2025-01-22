package ru.DTF98.TaskServiceWithAspect.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.DTF98.TaskServiceWithAspect.dto.TaskRequestDto;
import ru.DTF98.TaskServiceWithAspect.dto.TaskResponseDto;
import ru.DTF98.TaskServiceWithAspect.exception.NotFoundException;
import ru.DTF98.TaskServiceWithAspect.kafka.producer.KafkaTaskProducer;
import ru.DTF98.TaskServiceWithAspect.mapper.TaskMapper;
import ru.DTF98.TaskServiceWithAspect.model.Task;
import ru.DTF98.TaskServiceWithAspect.repository.TaskRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceUnitTest {

    @Spy
    private TaskMapper taskMapper = new TaskMapper();

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private KafkaTaskProducer kafkaTaskProducer;

    @InjectMocks
    private TaskService taskService;


    @Test
    @DisplayName("Создание новой задачи")
    public void createTask_whenInputValid_thenSave() {
        TaskRequestDto newTaskDto = new TaskRequestDto("Title", "Description", 1L);
        Task task = new Task(1L, "Title", "Description", 1L);

        TaskResponseDto expectedTaskDto = new TaskResponseDto(1L, "Title", "Description", 1L);

        when(taskRepository.save(any(Task.class)))
                .thenReturn(task);

        TaskResponseDto actualTaskResponseDto = taskService.createTask(newTaskDto);

        assertThat(actualTaskResponseDto.getId(), equalTo(expectedTaskDto.getId()));
    }

    @Test
    @DisplayName("Обновление задачи - успешный случай")
    public void updateTaskData_whenInputValid_thenUpdate() {
        TaskRequestDto taskRequest = new TaskRequestDto("Updated Title", "Updated Description", 1L);
        Task updatedTask = new Task(1L, "Updated Title", "Updated Description", 1L);

        TaskResponseDto expectedTaskDto = new TaskResponseDto(1L, "Updated Title", "Updated Description", 1L);

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(updatedTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        Mockito.doNothing().when(kafkaTaskProducer).sendTo(any(), any());

        TaskResponseDto actualTaskDto = taskService.updateTask(1L, taskRequest);

        assertThat(actualTaskDto, equalTo(expectedTaskDto));
    }

    @Test
    @DisplayName("Обновление задачи - не найдена задача")
    public void updateTaskData_whenTaskNotExists_thenThrowException() {
        TaskRequestDto taskRequest = new TaskRequestDto("Updated Title", "Updated Description", 1L);

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.updateTask(999L, taskRequest));
    }

    @Test
    @DisplayName("Удаление задачи - не найдена задача")
    public void deleteTask_whenTaskNotExists_thenThrowException() {
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.deleteTask(999L));
    }

    @Test
    @DisplayName("Получение задачи - успешный случай")
    public void getTask_whenInputValid_thenReturn() {
        Long taskId = 1L;

        Task task = new Task(1L, "Title", "Description", 1L);

        TaskResponseDto expectedTaskDto = new TaskResponseDto(1L, "Title", "Description", 1L);

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(task));

        TaskResponseDto actualTaskDto = taskService.getTaskById(taskId);

        assertThat(actualTaskDto, equalTo(expectedTaskDto));
    }

    @Test
    @DisplayName("Получение задачи - не найдена задача")
    public void getTask_whenTaskNotExists_thenThrowException() {
        Long taskId = 999L;

        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.getTaskById(taskId));
    }
}
