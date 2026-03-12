package com.example.taskmanagement.dto.Response;

import java.time.LocalDateTime;
import com.example.taskmanagement.Model.TaskStatus;
import lombok.Data;

@Data
public class TaskResponseDto {

    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private String remark;
}