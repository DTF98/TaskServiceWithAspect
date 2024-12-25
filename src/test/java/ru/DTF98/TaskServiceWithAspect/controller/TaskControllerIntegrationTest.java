package ru.DTF98.TaskServiceWithAspect.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.DTF98.TaskServiceWithAspect.model.Task;
import ru.DTF98.TaskServiceWithAspect.repository.TaskRepository;
import ru.DTF98.TaskServiceWithAspect.testContainer.FullIntegrationPropertyTestContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest extends FullIntegrationPropertyTestContainer {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Создание новой задачи")
    void testCreateTask() throws Exception {
        String requestBody = "{ \"title\": \"Test Task\", \"description\": \"Test Description\", \"userId\": 1 }";

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated()) // Проверяем код статуса 201
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("Получение задачи по ID")
    void testGetTaskById() throws Exception {
        Task task = new Task(null,"Test Task", "Test Description", 1L);
        task = taskRepository.save(task);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", task.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Проверяем код статуса 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(task.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("Обновление задачи")
    void testUpdateTask() throws Exception {
        Task task = new Task(null,"Old Title", "Old Description", 1L);
        task = taskRepository.save(task);

        String requestBody = "{ \"title\": \"Updated Task\", \"description\": \"Updated Description\", \"userId\": 2 }";

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Проверяем код статуса 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(2))
                .andDo(print());
    }

    @Test
    @DisplayName("Удаление задачи")
    void testDeleteTask() throws Exception {
        Task task = new Task(null,"Test Task", "Test Description", 1L);
        task = taskRepository.save(task);

        mockMvc.perform(MockMvcRequestBuilders.delete("/tasks/{id}", task.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent()) // Проверяем код статуса 204
                .andDo(print());
    }

    @Test
    @DisplayName("Получение всех задач")
    void testGetAllTasks() throws Exception {
        Task task1 = new Task(1L,"Task 1", "Description 1", 1L);
        Task task2 = new Task(2L,"Task 2", "Description 2", 2L);
        task1 = taskRepository.save(task1);
        task2 = taskRepository.save(task2);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Проверяем код статуса 200
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(task1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(task2.getId()))
                .andDo(print());
    }
}
