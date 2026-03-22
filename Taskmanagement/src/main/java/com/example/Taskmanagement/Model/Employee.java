package com.example.taskmanagement.Model;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Document(collection = "employees")
public class Employee {

    @Id
    private String employeeId;
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String password;
    private String roleId;
    private String departmentId;
    private String phone;
    private String designation;
    private String status;
    private String lastLogin;
    private boolean isDeleted;   
    private String createdBy;
    private String updatedBy;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String ProjectId;
   
}