package com.example.taskmanagement.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

public interface TaskService {

   PageResponse<TaskResponseDto> getAllTasks(
            String keyword,
            String status,
            String projectId,
            String milestoneId,
            String employeeId,
            String designation,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size);  

    TaskResponseDto addTask(TaskRequestDto dto);

    TaskResponseDto getTaskById(String id);

    String deleteTask(String id);

    TaskResponseDto updateTask(String id, TaskRequestDto dto);

    List<TaskResponseDto> getviewByStatus(String status);
    
    TaskResponseDto completeTask(String taskId);
    
    TaskResponseDto updateTaskStatus(String taskId, String status, String remarks);
}