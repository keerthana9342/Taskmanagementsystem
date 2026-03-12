package com.example.taskmanagement.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String employeeId;
    private String email;
    private String firstName;
    private String lastName;
    private String token;
    private String message;
}