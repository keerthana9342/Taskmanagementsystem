package com.example.taskmanagement.Service;

import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Request.LoginRequestDto;
import com.example.taskmanagement.dto.Response.AuthResponseDto;

public interface AuthService {
    AuthResponseDto login(LoginRequestDto loginRequest);
    AuthResponseDto register(EmployeeRequestDto registerRequest);
}