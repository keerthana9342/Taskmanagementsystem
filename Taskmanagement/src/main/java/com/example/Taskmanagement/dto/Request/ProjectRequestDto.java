package com.example.taskmanagement.dto.Request;

import java.util.List;

import lombok.Data;

@Data
public class ProjectRequestDto {
     private String projectName;
     private String projectId;
     private boolean isActive;
     private List<String>employeeIds;

}
