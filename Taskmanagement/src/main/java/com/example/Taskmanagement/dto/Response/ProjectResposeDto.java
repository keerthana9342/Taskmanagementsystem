package com.example.taskmanagement.dto.Response;

import java.util.List;

import lombok.Data;

@Data
public class ProjectResposeDto {
    private String projectName;
    private String projectId;
    private boolean isActive;
    private List<String>employeeIds;

}
