package com.example.taskmanagement.dto.Request;
import java.time.LocalDateTime;



import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class TaskRequestDto {

    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Status is required")
    private String status;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be present or future")
    private LocalDateTime dueDate;
    private String employeeId;
    private String projectId;

}

