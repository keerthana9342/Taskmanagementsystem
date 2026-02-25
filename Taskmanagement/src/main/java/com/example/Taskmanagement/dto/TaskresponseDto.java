package com.example.Taskmanagement.dto;

import java.time.LocalDate;

import com.example.Taskmanagement.Model.TaskStatus;

public class TaskresponseDto{

    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;

    // getters and setters
}
