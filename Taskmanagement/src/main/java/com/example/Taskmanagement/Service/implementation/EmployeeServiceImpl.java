package com.example.taskmanagement.Service.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Util.EmployeeMapper;
import com.example.taskmanagement.Config.JwtUtil;
import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.Model.Task;
import com.example.taskmanagement.Repository.EmployeeRepository;
import com.example.taskmanagement.Repository.ProjectRepository;
import com.example.taskmanagement.Repository.TaskRepository;
import com.example.taskmanagement.Repository.UserRepository;
import com.example.taskmanagement.Service.EmployeeService;
import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Request.LoginRequestDto;
import com.example.taskmanagement.dto.Response.AuthResponseDto;
import com.example.taskmanagement.dto.Response.EmployeeResponseDto;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskrepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final JwtUtil jwtUtil;
    private final MongoTemplate mongoTemplate;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               TaskRepository taskrepo,
                               PasswordEncoder passwordEncoder,
                                UserRepository userRepository,
                                ProjectRepository projectRepository,
                               JwtUtil jwtUtil,MongoTemplate mongoTemplate) {
        this.employeeRepository = employeeRepository;
        this.taskrepo = taskrepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.projectRepository = projectRepository;
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;   
    }

    @Override
    public EmployeeResponseDto addEmployee(EmployeeRequestDto dto) {
        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setUsername(dto.getUsername());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
        employee.setPhone(dto.getPhone());
        employee.setRoleId(dto.getRoleId());
        employee.setDepartmentId(dto.getDepartmentId());
        employee.setDesignation(dto.getDesignation());
        employee.setStatus(dto.getStatus());
        employee.setCreatedAt(LocalDate.now());
        employee.setDeleted(false);

        Employee saved = employeeRepository.save(employee);
        return EmployeeMapper.toDto(saved);
    }

@Override
public PageResponse<EmployeeResponseDto> getAllEmployees(
        String keyword,
        String status,
        String departmentId,
        String designation,
        String roleId,
        Pageable pageable) {

    Query query = new Query();
    List<Criteria> criteriaList = new ArrayList<>();

    //  Search keyword
    if (keyword != null && !keyword.trim().isEmpty()) {
        criteriaList.add(new Criteria().orOperator(
                Criteria.where("username").regex(keyword, "i"),
                Criteria.where("firstName").regex(keyword, "i"),
                Criteria.where("lastName").regex(keyword, "i"),
                Criteria.where("email").regex(keyword, "i"),
                Criteria.where("employeeId").regex(keyword, "i"),
                Criteria.where("departmentId").regex(keyword, "i"),
                Criteria.where("designation").regex(keyword, "i"),
                Criteria.where("status").regex(keyword, "i"),
                Criteria.where("roleId").regex(keyword, "i"),
                Criteria.where("phone").regex(keyword, "i")
        ));
    }

    // Filters
    if (status != null && !status.isEmpty()) {
        criteriaList.add(Criteria.where("status").is(status));
    }
    if (departmentId != null && !departmentId.isEmpty()) {
        criteriaList.add(Criteria.where("departmentId").is(departmentId));
    }
    if (designation != null && !designation.isEmpty()) {
        criteriaList.add(Criteria.where("designation").is(designation));
    }
    if (roleId != null && !roleId.isEmpty()) {
        criteriaList.add(Criteria.where("roleId").is(roleId));
    }

    //  Combine all criteria
    if (!criteriaList.isEmpty()) {
        query.addCriteria(new Criteria().andOperator(
                criteriaList.toArray(new Criteria[0])));
    }

    //  Get total count
    long total = mongoTemplate.count(query, Employee.class);

    //  Apply pagination
    query.with(pageable);
    List<Employee> employees = mongoTemplate.find(query, Employee.class);

    List<EmployeeResponseDto> employeeDtos = employees.stream()
            .map(EmployeeMapper::toDto)
            .toList();

    return new PageResponse<>(
            employeeDtos,
            pageable.getPageNumber(),
            pageable.getPageSize(),
            total,
            (int) Math.ceil((double) total / pageable.getPageSize())
    );
}
    @Override
public String deleteEmployee(String employeeId) {

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

    // Soft delete
    employee.setDeleted(true);

    employeeRepository.save(employee);

    return "Employee deleted successfully";
}
@Override
public EmployeeResponseDto updateEmployee(String employeeId, EmployeeRequestDto dto) {

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

    // Update fields
    employee.setUsername(dto.getUsername());
    employee.setFirstName(dto.getFirstName());
    employee.setLastName(dto.getLastName());
    employee.setEmail(dto.getEmail());
    employee.setPhone(dto.getPhone());
    employee.setRoleId(dto.getRoleId());
    employee.setDepartmentId(dto.getDepartmentId());
    employee.setDesignation(dto.getDesignation());
    employee.setStatus(dto.getStatus());
    

    //  Update password only if provided
    if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
        employee.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    Employee updatedEmployee = employeeRepository.save(employee);

    return EmployeeMapper.toDto(updatedEmployee);
}
@Override
public void hardDeleteEmployee(String employeeId) {

    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

    //  1. Delete User (correct way)
    userRepository.findByEmployeeId(employeeId)
            .ifPresent(userRepository::delete);

    //  2. Delete Tasks
    taskrepo.deleteByAssignedToEmployeeId(employeeId);

    //  3. Remove employee from projects
    List<Project> projects = projectRepository.findAll();

    for (Project project : projects) {
        if (project.getEmployeeIds() != null) {
            project.getEmployeeIds().remove(employeeId);
            projectRepository.save(project);
        }
    }

    // 4. Delete Employee
    employeeRepository.delete(employee);
}


    @Override
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        Employee employee = employeeRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), employee.getPassword())) {
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
        if (employeeRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        EmployeeResponseDto employeeResponse = addEmployee(registerRequest);
        Employee employee = employeeRepository.findByEmail(registerRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found after registration"));
        
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
