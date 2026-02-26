package com.example.Taskmanagement.Model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="TaskManagementSystem")
public class Task {

    @Id
    private String id;
    private String Title;
    private String Description;
    private String Status;
    private LocalDate duedate;
}

   