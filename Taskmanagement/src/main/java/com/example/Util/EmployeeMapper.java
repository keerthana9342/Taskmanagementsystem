package com.example.Util;

import java.util.stream.Collectors;

import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.dto.Request.EmployeeRequestDto;
import com.example.taskmanagement.dto.Response.EmployeeResponseDto;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRequestDto dto) {

        Employee employee = new Employee();

        employee.setEmployeeId(dto.getEmployeeId());
        employee.setUsername(dto.getUsername());
        employee.setEmail(dto.getEmail());

        return employee;
    }

    public static EmployeeResponseDto toDto(Employee employee){

        EmployeeResponseDto dto = new EmployeeResponseDto();

        dto.setEmployeeId(employee.getEmployeeId());
        dto.setUsername(employee.getUsername());
        dto.setEmail(employee.getEmail());
        

        if(employee.getTasks()!=null){
            dto.setTaskIds(
                    employee.getTasks()
                    .stream()
                    .map(task -> task.getId())
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

}

