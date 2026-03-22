package com.example.taskmanagement.Service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.Util.DepartmentMapper;
import com.example.taskmanagement.Model.Department;
import com.example.taskmanagement.Repository.DepartmentRepository;
import com.example.taskmanagement.Service.DepartmentService;
import com.example.taskmanagement.dto.Request.DepartmentRequestDto;
import com.example.taskmanagement.dto.Response.DepartmentResponseDto;

public class DepartmentServiceImpl implements DepartmentService {

 @Autowired
    private DepartmentRepository departmentRepository;

    // Get All Departments (Pagination)
    @Override
    public List<DepartmentResponseDto> getAllDepartments(int page, int size) {

        Page<Department> departmentPage =
                departmentRepository.findAll(PageRequest.of(page, size));

        return departmentPage.getContent()
                .stream()
                .map(DepartmentMapper::toResponseDto)
                .toList();
    }

    // Add Department
    @Override
    public DepartmentResponseDto addDepartment(DepartmentRequestDto dto) {

        Department department = DepartmentMapper.toEntity(dto);

        Department savedDepartment = departmentRepository.save(department);

        return DepartmentMapper.toResponseDto(savedDepartment);
    }

    // Get Department by ID
    @Override
    public DepartmentResponseDto getDepartmentById(String id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        return DepartmentMapper.toResponseDto(department);
    }

    // Delete Department
    @Override
    public String deleteDepartment(String id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        departmentRepository.delete(department);

        return "Department deleted successfully";
    }

    // Update Department
    @Override
    public DepartmentResponseDto updateDepartment(String id, DepartmentRequestDto dto) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.setDepartmentName(dto.getDepartmentName());
        department.setDescription(dto.getDescription());

        Department updatedDepartment = departmentRepository.save(department);

        return DepartmentMapper.toResponseDto(updatedDepartment);
    }
}




