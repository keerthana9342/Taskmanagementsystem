package com.example.Taskmanagement.dto;

import java.time.LocalDateTime;
import com.example.Taskmanagement.Model.TaskStatus;
import lombok.Data;

@Data
public class TaskresponseDto {

    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDate;
}