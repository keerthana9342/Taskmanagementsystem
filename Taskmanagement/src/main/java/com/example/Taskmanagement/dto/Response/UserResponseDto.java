package com.example.taskmanagement.dto.Response;

import lombok.Data;

@Data
public class UserResponseDto {
    private String id;
    private String userId;
    private String username;
    private String email;
    private String role;
    private String status;

}
