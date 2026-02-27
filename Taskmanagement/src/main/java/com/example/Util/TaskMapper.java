package com.example.Util;

import com.example.Taskmanagement.Model.Task;
import com.example.Taskmanagement.dto.TaskrequestDto;
import com.example.Taskmanagement.dto.TaskresponseDto;

public class TaskMapper {

    public static Task toEntity(TaskrequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());
        return task;
    }

    public static TaskresponseDto toDTO(Task task) {
        TaskresponseDto dto = new TaskresponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        return dto;
    }
}
