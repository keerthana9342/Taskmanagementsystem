package com.example.taskmanagement.dto.Request;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class AssignedEmployeeDto {

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Username is required")
    private String username;

    private Boolean isActive;

    private String designation;
    
}


