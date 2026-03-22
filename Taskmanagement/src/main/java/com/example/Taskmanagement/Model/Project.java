package com.example.taskmanagement.Model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
@Document(collection = "projects")
public class Project {
    @Id
    private String projectId;
    private String projectName;
    private boolean isActive;
    private List<String> employeeIds;
    
}
