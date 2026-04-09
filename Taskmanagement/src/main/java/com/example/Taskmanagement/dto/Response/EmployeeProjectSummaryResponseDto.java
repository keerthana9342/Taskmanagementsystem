package com.example.taskmanagement.dto.Response;

import java.util.List;

import lombok.Data;
@Data
public class EmployeeProjectSummaryResponseDto {
    private String employeeId;
    private String username;
    private String designation;
    private List<ProjectTaskResponseDto> projects;

}
    