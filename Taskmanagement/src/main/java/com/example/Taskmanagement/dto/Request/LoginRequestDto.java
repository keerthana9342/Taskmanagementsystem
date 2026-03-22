package com.example.taskmanagement.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @Email
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
    

    
} 