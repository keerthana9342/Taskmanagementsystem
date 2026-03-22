package com.example.Util;
import com.example.taskmanagement.Model.Department;
import com.example.taskmanagement.dto.Request.DepartmentRequestDto;
import com.example.taskmanagement.dto.Response.DepartmentResponseDto;

public class DepartmentMapper{
    public static Department toEntity(DepartmentRequestDto departmentRequestDto){
        Department department = new Department();
        department.setDepartmentName(departmentRequestDto.getDepartmentName());
        department.setDescription(departmentRequestDto.getDescription());
        return department;
    }
    public static DepartmentResponseDto toResponseDto(Department department){
        DepartmentResponseDto departmentResponseDto = new DepartmentResponseDto();
        departmentResponseDto.setId(department.getId());
        departmentResponseDto.setDepartmentName(department.getDepartmentName());
        departmentResponseDto.setDescription(department.getDescription());
        return departmentResponseDto;
    }
}
