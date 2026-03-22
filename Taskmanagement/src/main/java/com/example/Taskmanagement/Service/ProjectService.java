package com.example.taskmanagement.Service;

import java.util.List;

import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.dto.Response.ProjectResposeDto;

public interface ProjectService {

    Project addProject(Project project);

    List<Project> getAllProjects();

    Project getProjectById(String projectId);

    Project updateProject(String projectId, Project project);

    String deleteProject(String projectId);
    public ProjectResposeDto assignEmployees(String projectId,List<String>employeeIds);
}