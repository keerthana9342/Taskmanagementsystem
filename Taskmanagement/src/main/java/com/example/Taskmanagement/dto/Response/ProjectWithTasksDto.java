package com.example.taskmanagement.dto.Response;

import java.util.List;
import lombok.Data;

@Data
public class ProjectWithTasksDto {
    private String projectId;       // project id
    private String projectName;     // project name
    private boolean isActive;       // is project active
    private List<String> employeeIds;              // employees in project
    private List<MileStoneResponseDto> mileStones; 
}
