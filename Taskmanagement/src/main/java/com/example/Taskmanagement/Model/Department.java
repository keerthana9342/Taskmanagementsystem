package com.example.taskmanagement.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "departments")
public class Department {
@Id
    private String id;
    private String departmentName;
    private String description;
    private String createdBy;
    private String createdAt;
    private String updatedAt;
    private  String updatedBy;

}
