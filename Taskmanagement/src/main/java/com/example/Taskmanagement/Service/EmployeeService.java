package com.example.taskmanagement.Service;
import org.springframework.data.domain.Pageable;
import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Request.LoginRequestDto;
import com.example.taskmanagement.dto.Response.AuthResponseDto;
import com.example.taskmanagement.dto.Response.EmployeeResponseDto;

public interface EmployeeService {

    EmployeeResponseDto addEmployee(EmployeeRequestDto dto);


        PageResponse<EmployeeResponseDto> getAllEmployees(
            String keyword,
            String status,
            String departmentId,
            String designation,
            String roleId,
            Pageable pageable);



    AuthResponseDto login(LoginRequestDto loginRequest);
    
    AuthResponseDto register(EmployeeRequestDto registerRequest);
    
    
    String deleteEmployee(String employeeId);//soft delete
    EmployeeResponseDto updateEmployee(String employeeId, EmployeeRequestDto dto);
    void hardDeleteEmployee(String employeeId);
}
