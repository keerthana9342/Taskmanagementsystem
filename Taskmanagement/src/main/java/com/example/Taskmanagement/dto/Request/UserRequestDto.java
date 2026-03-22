package com.example.taskmanagement.dto.Request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotNull(message = "User ID is required")
    private String userId;
    @NotBlank
    private String username;
    @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String role;
    
    private String status;

}
