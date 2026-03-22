package com.example.taskmanagement.Model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "users")
public class User {
    private String id;
    private String userId;
    private String username;
    private String email;
    private String password;
    private String role;
    private String status;
    private String employeeId;
    private Boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}