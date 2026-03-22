package com.example.Util;

import com.example.taskmanagement.Model.Task;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

public class TaskMapper {

    public static Task toEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        task.setDueDate(dto.getDueDate());
        task.setEmployeeId(dto.getEmployeeId());
        task.setProjectId(dto.getProjectId());
        task.setIsDeleted(false);
        return task;
    }

    public static TaskResponseDto toDTO(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setEmployeeId(task.getEmployeeId());
        dto.setProjectId(task.getProjectId());
        dto.setRemarks(task.getRemarks());
        dto.setStatus(task.getStatus());
        dto.setCompletedDate(task.getCompletedDate());
       return dto;
    }
}