package com.example.taskmanagement.Service;

import java.util.List;

import com.example.taskmanagement.Model.TaskStatus;
import com.example.taskmanagement.dto.Request.TaskRequestDto;

public interface TaskService {

    List<com.example.taskmanagement.dto.Response.TaskResponseDto> getAllTasks(int page, int size);

    com.example.taskmanagement.dto.Response.TaskResponseDto addTask(TaskRequestDto dto);

    com.example.taskmanagement.dto.Response.TaskResponseDto getTaskById(String id);

    String deleteTask(String id);

    com.example.taskmanagement.dto.Response.TaskResponseDto updateTask(String id, TaskRequestDto dto);

    List<com.example.taskmanagement.dto.Response.TaskResponseDto> getViewByStatus(TaskStatus status);
}