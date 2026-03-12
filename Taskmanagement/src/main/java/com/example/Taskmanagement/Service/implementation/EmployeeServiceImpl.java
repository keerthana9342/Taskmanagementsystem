package com.example.taskmanagement.Service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Util.EmployeeMapper;
import com.example.taskmanagement.Config.JwtUtil;
import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.Model.Task;
import com.example.taskmanagement.Repository.EmployeeRepository;
import com.example.taskmanagement.Repository.TaskRepository;
import com.example.taskmanagement.Service.EmployeeService;
import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Request.LoginRequestDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.AuthResponseDto;
import com.example.taskmanagement.dto.Response.EmployeeResponseDto;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskrepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               TaskRepository taskrepo,
                               PasswordEncoder passwordEncoder,
                               JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.taskrepo = taskrepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Add Employee
    @Override
    public EmployeeResponseDto addEmployee(EmployeeRequestDto dto) {

        Employee employee = EmployeeMapper.toEntity(dto);

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        Employee saved = employeeRepository.save(employee);

        return EmployeeMapper.toDto(saved);
    }

    // Get Employees with Pagination
    @Override
    public PageResponse<EmployeeResponseDto> getAllEmployees(Pageable pageable) {

        Page<Employee> page = employeeRepository.findAll(pageable);

        List<EmployeeResponseDto> employees = page.getContent()
                .stream()
                .map(EmployeeMapper::toDto)
                .toList();

        return new PageResponse<>(
                employees,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    // Assign Tasks
    @Override
    public Employee assignTasks(String employeeId, List<TaskRequestDto> taskDtos) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Task> tasks = new ArrayList<>();

        for (TaskRequestDto dto : taskDtos) {

            Task task = new Task();
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setStatus(dto.getStatus());
            task.setDueDate(dto.getDueDate());

            tasks.add(taskrepo.save(task));
        }

        employee.setTasks(tasks);

        return employeeRepository.save(employee);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail());
        
        if (employee == null || !passwordEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        String token = jwtUtil.generateToken(employee.getEmail());
        
        return new AuthResponseDto(
            employee.getEmployeeId(),
            employee.getEmail(),
            employee.getFirstName(),
            employee.getLastName(),
            token,
            "Login successful"
        );
    }

    @Override
    public AuthResponseDto register(EmployeeRequestDto registerRequest) {
        if (employeeRepository.findByEmail(registerRequest.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        
        EmployeeResponseDto employeeResponse = addEmployee(registerRequest);
        Employee employee = employeeRepository.findByEmail(registerRequest.getEmail());
        
        String token = jwtUtil.generateToken(employee.getEmail());
        
        return new AuthResponseDto(
            employee.getEmployeeId(),
            employee.getEmail(),
            employee.getFirstName(),
            employee.getLastName(),
            token,
            "Registration successful"
        );
    }
}