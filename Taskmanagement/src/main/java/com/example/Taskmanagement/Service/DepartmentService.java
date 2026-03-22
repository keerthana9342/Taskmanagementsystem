package com.example.taskmanagement.Service;
import java.util.List;
import com.example.taskmanagement.dto.Request.DepartmentRequestDto;
import com.example.taskmanagement.dto.Response.DepartmentResponseDto;
public interface DepartmentService {
    List<DepartmentResponseDto> getAllDepartments(int page, int size);
    DepartmentResponseDto addDepartment(DepartmentRequestDto dto);
    DepartmentResponseDto getDepartmentById(String id);
    String deleteDepartment(String id);
    DepartmentResponseDto updateDepartment(String id, DepartmentRequestDto dto);

}
