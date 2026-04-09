package com.example.taskmanagement.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.taskmanagement.Service.AuthService;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Request.LoginRequestDto;
import com.example.taskmanagement.dto.Response.AuthResponseDto;

import jakarta.validation.Valid;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        AuthResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody EmployeeRequestDto registerRequest) {
        AuthResponseDto response = authService.register(registerRequest);
        return ResponseEntity.ok(response);
    }
}