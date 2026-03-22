package com.example.taskmanagement.Service.implementation;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskmanagement.Config.JwtUtil;
import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.Model.User;
import com.example.taskmanagement.Repository.EmployeeRepository;
import com.example.taskmanagement.Repository.UserRepository;
import com.example.taskmanagement.Service.AuthService;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Request.LoginRequestDto;
import com.example.taskmanagement.dto.Response.AuthResponseDto;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(EmployeeRepository employeeRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        // Find user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Check if user is active (null check for backward compatibility)
        if (user.getIsActive() != null && !user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }

        // Find employee details
        Employee employee = employeeRepository.findById(user.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDto(
                employee.getEmployeeId(),
                user.getEmail(),
                employee.getFirstName(),
                employee.getLastName(),
                token,
                "Login successful"
        );
    }

    @Override
    public AuthResponseDto register(EmployeeRequestDto registerRequest) {
        // Check if email already exists in User table
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Check if email already exists in Employee table
        if (employeeRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Create Employee (without encoding password in mapper)
        Employee employee = new Employee();
        employee.setEmployeeId(registerRequest.getEmployeeId());
        employee.setUsername(registerRequest.getUsername());
        employee.setFirstName(registerRequest.getFirstName());
        employee.setLastName(registerRequest.getLastName());
        employee.setEmail(registerRequest.getEmail());
        employee.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Encode here
        employee.setPhone(registerRequest.getPhone());
        employee.setRoleId(registerRequest.getRoleId());
        employee.setDepartmentId(registerRequest.getDepartmentId());
        employee.setDesignation(registerRequest.getDesignation());
        employee.setStatus(registerRequest.getStatus());
        employee.setCreatedAt(LocalDate.now());
        employee.setDeleted(false);
        
        Employee savedEmployee = employeeRepository.save(employee);

        // Create User
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("EMPLOYEE");
        user.setEmployeeId(savedEmployee.getEmployeeId());
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setDeleted(false);

        userRepository.save(user);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponseDto(
                savedEmployee.getEmployeeId(),
                user.getEmail(),
                savedEmployee.getFirstName(),
                savedEmployee.getLastName(),
                token,
                "Registration successful"
        );
    }
}