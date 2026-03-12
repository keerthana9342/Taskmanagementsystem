package com.example.Util;

import java.time.LocalDateTime;

import com.example.taskmanagement.Model.Task;
import com.example.taskmanagement.Model.TaskStatus;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

public class TaskMapper {

    public static Task toEntity(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());
        return task;
    }

    public static TaskResponseDto toDTO(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setRemark(getRemark(task));
        return dto;
    }

 private static String getRemark(Task task){

        if(task.getStatus() == TaskStatus.COMPLETED){
            return "Task Completed";
        }

        if(task.getDueDate().isBefore(LocalDateTime.now())){
            return "Task Overdue";
        }

        return "Task Pending";
    }
}
