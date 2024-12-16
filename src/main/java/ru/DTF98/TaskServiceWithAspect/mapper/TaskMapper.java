package ru.DTF98.TaskServiceWithAspect.mapper;

import org.springframework.stereotype.Component;
import ru.DTF98.TaskServiceWithAspect.dto.TaskRequestDto;
import ru.DTF98.TaskServiceWithAspect.dto.TaskResponseDto;
import ru.DTF98.TaskServiceWithAspect.model.Task;

@Component
public class TaskMapper {
    public TaskResponseDto toResponse(Task task) {
        TaskResponseDto responseDto = new TaskResponseDto();
        if (task.getId() != null) {
            responseDto.setId(task.getId());
        }
        if (task.getDescription() != null) {
            responseDto.setDescription(task.getDescription());
        }
        if (task.getTitle() != null) {
            responseDto.setTitle(task.getTitle());
        }
        if (task.getUserId() != null) {
            responseDto.setUserId(task.getUserId());
        }
        return responseDto;
    }

    public Task toModelFromRequest(TaskRequestDto requestDto) {
        Task task = new Task();
        if (requestDto.getTitle() != null) {
            task.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null) {
            task.setDescription(requestDto.getDescription());
        }
        if (requestDto.getUserId() != null) {
            task.setUserId(requestDto.getUserId());
        }
        return task;
    }

    public Task toModelFromResponse(TaskResponseDto responseDto) {
        Task task = new Task();
        if (responseDto.getId() != null) {
            task.setId(responseDto.getId());
        }
        if (responseDto.getDescription() != null) {
            task.setDescription(responseDto.getDescription());
        }
        if (responseDto.getTitle() != null) {
            task.setTitle(responseDto.getTitle());
        }
        if (responseDto.getUserId() != null) {
            task.setUserId(responseDto.getUserId());
        }
        return task;
    }
}
