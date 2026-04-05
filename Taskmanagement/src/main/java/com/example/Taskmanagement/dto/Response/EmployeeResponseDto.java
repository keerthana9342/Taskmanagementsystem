package com.example.taskmanagement.dto.Response;
import lombok.Data;

@Data
public class EmployeeResponseDto {
    private String employeeId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String roleId;
    private String departmentId;
    private String phone;
    private String designation;
    private String status;
    
    
}