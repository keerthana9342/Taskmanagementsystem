package com.example.Taskmanagement.dto;

import java.time.LocalDateTime;

import com.example.Taskmanagement.Model.TaskStatus;

public class TaskrequestDto {

    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDate;

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}

