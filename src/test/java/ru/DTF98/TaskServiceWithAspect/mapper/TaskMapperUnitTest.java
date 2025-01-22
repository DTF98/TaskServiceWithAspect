package ru.DTF98.TaskServiceWithAspect.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.DTF98.TaskServiceWithAspect.dto.TaskRequestDto;
import ru.DTF98.TaskServiceWithAspect.dto.TaskResponseDto;
import ru.DTF98.TaskServiceWithAspect.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskMapperUnitTest {
    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }

    @Test
    void testToResponse_ShouldMapTaskToTaskResponseDto() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Title");
        task.setDescription("Test Description");
        task.setUserId(100L);

        TaskResponseDto responseDto = taskMapper.toResponse(task);

        assertEquals(task.getId(), responseDto.getId());
        assertEquals(task.getTitle(), responseDto.getTitle());
        assertEquals(task.getDescription(), responseDto.getDescription());
        assertEquals(task.getUserId(), responseDto.getUserId());
    }

    @Test
    void testToModelFromRequest_ShouldMapTaskRequestDtoToTask() {
        TaskRequestDto requestDto = new TaskRequestDto("Request Title", "Request Description", 101L);

        Task task = taskMapper.toModelFromRequest(requestDto);

        assertEquals(requestDto.getTitle(), task.getTitle());
        assertEquals(requestDto.getDescription(), task.getDescription());
        assertEquals(requestDto.getUserId(), task.getUserId());
    }

    @Test
    void testToModelFromResponse_ShouldMapTaskResponseDtoToTask() {
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Response Title");
        responseDto.setDescription("Response Description");
        responseDto.setUserId(102L);

        Task task = taskMapper.toModelFromResponse(responseDto);

        assertEquals(responseDto.getId(), task.getId());
        assertEquals(responseDto.getTitle(), task.getTitle());
        assertEquals(responseDto.getDescription(), task.getDescription());
        assertEquals(responseDto.getUserId(), task.getUserId());
    }

    @Test
    void testToResponse_ShouldNotSetNullValues() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Title");

        TaskResponseDto responseDto = taskMapper.toResponse(task);

        assertEquals(task.getId(), responseDto.getId());
        assertEquals(task.getTitle(), responseDto.getTitle());
        assertNull(responseDto.getDescription());
        assertNull(responseDto.getUserId());
    }

    @Test
    void testToModelFromResponse_ShouldNotSetNullValues() {
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Response Title");

        Task task = taskMapper.toModelFromResponse(responseDto);

        assertEquals(responseDto.getId(), task.getId());
        assertEquals(responseDto.getTitle(), task.getTitle());
        assertNull(task.getDescription());
        assertNull(task.getUserId());
    }
}
