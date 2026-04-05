package com.example.taskmanagement.Controller;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanagement.Service.EmployeeService;
import com.example.taskmanagement.dto.APIresponse;
import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.AssignTaskRequestDto;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Response.EmployeeResponseDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employee")
@CrossOrigin(origins = "*", maxAge = 3600)
public class Employeecontroller {

    @Autowired
    private EmployeeService employeeService;
     
    // Add Employee
    @PostMapping("/add")
    public ResponseEntity<EmployeeResponseDto> addEmployee(@Valid @RequestBody EmployeeRequestDto dto) {
        return ResponseEntity.ok(employeeService.addEmployee(dto));
    }

  // Get Employees with Pagination
@GetMapping("/get")
public ResponseEntity<APIresponse<PageResponse<EmployeeResponseDto>>> getAllEmployees(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    PageResponse<EmployeeResponseDto> employees =
            employeeService.getAllEmployees(PageRequest.of(page, size));

    APIresponse<PageResponse<EmployeeResponseDto>> response =
            new APIresponse<>("SUCCESS",
                    "Employees fetched successfully",
                    employees,
                    LocalDateTime.now());

    return ResponseEntity.ok(response);
}
@PutMapping("/update/{employeeId}")
public ResponseEntity<?> updateEmployee(
        @PathVariable String employeeId,
        @RequestBody EmployeeRequestDto dto) {

    return ResponseEntity.ok(employeeService.updateEmployee(employeeId, dto));
}
  
    @DeleteMapping("/delete/{employeeId}")
public ResponseEntity<?> deleteEmployee(@PathVariable String employeeId) {
    return ResponseEntity.ok(employeeService.deleteEmployee(employeeId));
}
@DeleteMapping("/hard-delete/{employeeId}")
public ResponseEntity<?> hardDeleteEmployee(@PathVariable String employeeId) {
    employeeService.hardDeleteEmployee(employeeId);
    return ResponseEntity.ok("Employee permanently deleted");
}

    
}

    





    
    


