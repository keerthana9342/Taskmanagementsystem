package com.example.taskmanagement.dto.Response;
import java.util.List;
import lombok.Data;

@Data
public class EmployeeResponseDto {
    private String employeeId;
    private String username;
    private String email;
    private List<String> taskIds;
    

}


