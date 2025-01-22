package ru.DTF98.TaskServiceWithAspect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.DTF98.TaskServiceWithAspect.dto.TaskRequestDto;
import ru.DTF98.TaskServiceWithAspect.exception.NotFoundException;
import ru.DTF98.TaskServiceWithAspect.service.TaskService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
public class TaskControllerValidationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Создание новой задачи — заголовок не указан")
    void createTask_shouldReturnBadRequestWhenTitleIsMissing() throws Exception {
        TaskRequestDto taskRequest = new TaskRequestDto("", "Description", 1L);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание новой задачи — описание не указано")
    void createTask_shouldReturnBadRequestWhenDescriptionIsMissing() throws Exception {
        TaskRequestDto taskRequest = new TaskRequestDto("Title", "", 1L);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание новой задачи — пользователь не указан")
    void createTask_shouldReturnBadRequestWhenOwnerIsMissing() throws Exception {
        TaskRequestDto taskRequest = new TaskRequestDto("Title", "Description", null);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Получение задачи по ID — задача не найдена")
    void getTaskById_shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        when(taskService.getTaskById(1L)).thenThrow(new NotFoundException("Task not found"));

        mockMvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Удаление задачи — задача не найдена")
    void deleteTask_shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        doThrow(new NotFoundException("Task not found")).when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
