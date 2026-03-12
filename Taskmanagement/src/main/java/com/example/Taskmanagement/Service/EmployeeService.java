package com.example.taskmanagement.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Request.LoginRequestDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.AuthResponseDto;
import com.example.taskmanagement.dto.Response.EmployeeResponseDto;

public interface EmployeeService {

    EmployeeResponseDto addEmployee(EmployeeRequestDto dto);

    PageResponse<EmployeeResponseDto> getAllEmployees(Pageable pageable);

    Employee assignTasks(String employeeId, List<TaskRequestDto> taskDtos);

    AuthResponseDto login(LoginRequestDto loginRequest);
    
    AuthResponseDto register(EmployeeRequestDto registerRequest);
}
