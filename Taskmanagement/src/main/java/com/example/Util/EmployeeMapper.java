package com.example.Util;

import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Response.EmployeeResponseDto;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequestDto dto) {

        Employee employee = new Employee();

        employee.setEmployeeId(dto.getEmployeeId());
        employee.setUsername(dto.getUsername());
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setPassword(dto.getPassword());   
        employee.setRoleId(dto.getRoleId());
        employee.setDepartmentId(dto.getDepartmentId());
        employee.setDesignation(dto.getDesignation());
        employee.setStatus(dto.getStatus());
        employee.setProjectId(dto.getProjectId());

        return employee;
    }

    public static EmployeeResponseDto toDto(Employee employee){

        EmployeeResponseDto dto = new EmployeeResponseDto();

        dto.setEmployeeId(employee.getEmployeeId());
        dto.setUsername(employee.getUsername());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setRoleId(employee.getRoleId());
        dto.setDepartmentId(employee.getDepartmentId());
        dto.setDesignation(employee.getDesignation());
        dto.setStatus(employee.getStatus());
        dto.setProjectId(employee.getProjectId());
         
        return dto;
    }

}

