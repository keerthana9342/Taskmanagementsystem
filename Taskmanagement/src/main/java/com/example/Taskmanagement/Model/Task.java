package com.example.taskmanagement.Model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.taskmanagement.dto.Request.AssignedEmployeeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Data
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;
    
    private String title;

    private String description;

    
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    
    private LocalDateTime dueDate;
    @NotBlank(message = "ProjectId is required")
    private String projectId;
    private LocalDateTime completedDate;
    private String remarks;
    private Boolean isDeleted =false;
    private String milestoneId;
    private AssignedEmployeeDto assignedTo; 
    private AssignedEmployeeDto assignedBy; 

}