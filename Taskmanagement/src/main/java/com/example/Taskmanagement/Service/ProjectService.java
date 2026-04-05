package com.example.taskmanagement.Service;

import java.util.List;
import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.dto.Request.MileStoneRequestDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.MileStoneResponseDto;
import com.example.taskmanagement.dto.Response.ProjectResposeDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

public interface ProjectService {

    // Project
    Project addProject(Project project);
    List<Project> getAllProjects();
    Project getProjectById(String projectId);
    Project updateProject(String projectId, Project project);
    String deleteProject(String projectId);
    ProjectResposeDto assignEmployees(String projectId, List<String> employeeIds);

    // Milestone
    MileStoneResponseDto addMilestone(String projectId, MileStoneRequestDto dto);
    MileStoneResponseDto updateMilestone(String projectId, String milestoneId, MileStoneRequestDto dto);
    String deleteMilestone(String projectId, String milestoneId);
    List<MileStoneResponseDto> getMilestonesByProjectId(String projectId);
    MileStoneResponseDto getMilestoneById(String projectId, String milestoneId);

    // Task
    TaskResponseDto addTask(String milestoneId, String projectId, TaskRequestDto dto);
    TaskResponseDto updateTask(String taskId, TaskRequestDto dto);
    String deleteTask(String taskId);
    List<TaskResponseDto> getTasksByMilestoneId(String milestoneId);
}