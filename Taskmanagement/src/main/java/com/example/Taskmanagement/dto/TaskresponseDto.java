package com.example.Taskmanagement.dto;

import java.time.LocalDate;

import com.example.Taskmanagement.Model.TaskStatus;

public class TaskresponseDto{

    private String id;
    private String Title;
    private String Description;
    private TaskStatus Status;
    private LocalDate dueDate;

    
}
