package com.example.taskmanagement.dto.Response;

import java.time.LocalDateTime;
import com.example.taskmanagement.dto.Request.AssignedEmployeeDto;

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
    private String projectId;
    private Boolean isDeleted;
    private String milestoneId;
    private EmployeeProjectSummaryResponseDto assignedTo;    
    private AssignedEmployeeDto assignedBy; 


}