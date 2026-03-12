package com.example.taskmanagement.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class APIresponse<T> {

    private String status;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    

    public APIresponse(String status, String message, T data, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }
}