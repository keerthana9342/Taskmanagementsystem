package com.example.taskmanagement.Service;

import java.util.List;

import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

public interface TaskService {

    List<TaskResponseDto> getAllTasks(int page, int size);

    TaskResponseDto addTask(TaskRequestDto dto);

    TaskResponseDto getTaskById(String id);

    String deleteTask(String id);

    TaskResponseDto updateTask(String id, TaskRequestDto dto);

    List<TaskResponseDto> getviewByStatus(String status);
    
    TaskResponseDto completeTask(String taskId);
    
    TaskResponseDto updateTaskStatus(String taskId, String status, String remarks);
}