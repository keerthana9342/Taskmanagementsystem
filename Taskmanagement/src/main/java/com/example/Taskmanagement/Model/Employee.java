package com.example.taskmanagement.Model;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String FirstName;
    private String LastName;
    private String roleId;
    private String departmentId;
    private String phone;
    private String desiganation;
    private String status;
    private String lastLogin;
    private String isDeleted;   
    private String createdBy;
    private String updatedBy;
    private LocalDate createdAt;
    private LocalDate updatedAt;
   @DBRef
    private List<Task> tasks;

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}


