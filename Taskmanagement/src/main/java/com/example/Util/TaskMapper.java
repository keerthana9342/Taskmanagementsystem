package com.example.Util;


import com.example.taskmanagement.Model.Task;

import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

public class TaskMapper {

    //  Request DTO → Entity
    public static Task toEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        task.setDueDate(dto.getDueDate());
        task.setProjectId(dto.getProjectId());
        task.setMilestoneId(dto.getMilestoneId());
        task.setRemarks(dto.getRemarks());
        task.setIsDeleted(false);

        //  Map assignedTo from request body
        if (dto.getAssignedTo() != null) {
            task.setAssignedTo(dto.getAssignedTo());
        }

        return task;
    }

    //  Entity → Response DTO
    public static TaskResponseDto toDTO(Task task) {
        if (task == null) return null;

        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setProjectId(task.getProjectId());
        dto.setMilestoneId(task.getMilestoneId());
        dto.setDueDate(task.getDueDate());
        dto.setCompletedDate(task.getCompletedDate());
        dto.setRemarks(task.getRemarks());
        dto.setIsDeleted(task.getIsDeleted());

        

        // Map assignedBy (from token - already set in service)
        dto.setAssignedBy(task.getAssignedBy());

        return dto;
    }
}