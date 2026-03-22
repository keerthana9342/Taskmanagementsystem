package com.example.taskmanagement.dto.Response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TaskResponseDto {

    private String id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime dueDate;
    private String remarks;
    private LocalDateTime completedDate;
    private String employeeId;
    private String projectId;


}