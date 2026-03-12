package com.example.taskmanagement.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeRequestDto {
    @NotBlank(message = "Employee ID is required")  
    private String employeeId;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
}


