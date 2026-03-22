package com.example.taskmanagement.Model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Data
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Status is required")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be present or future")
    private LocalDateTime dueDate;
    private String employeeId;
    private String projectId;
    private LocalDateTime completedDate;
    private String remarks;
    private Boolean isDeleted =false;

}