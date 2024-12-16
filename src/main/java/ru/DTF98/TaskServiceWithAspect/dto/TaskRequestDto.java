package ru.DTF98.TaskServiceWithAspect.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private Long userId;
}
