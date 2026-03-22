package com.example.taskmanagement.Service.implementation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.Util.ProjectMapper;
import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.Repository.ProjectRepository;
import com.example.taskmanagement.Service.ProjectService;
import com.example.taskmanagement.dto.Response.ProjectResposeDto;
@Service
public class ProjectImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    public ProjectImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        
    }
    
    @Override
    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(String projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
    }

    @Override
    public Project updateProject(String projectId, Project project) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        existingProject.setProjectName(project.getProjectName());
        existingProject.setActive(project.isActive());

        return projectRepository.save(existingProject);
    }

    @Override
    public String deleteProject(String projectId) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        projectRepository.delete(existingProject);
        return "Project deleted successfully";
    }
    @Override
    public ProjectResposeDto assignEmployees(String projectId, List<String> employeeIds){
        Project existingProject = projectRepository.findById(projectId)
        .orElseThrow(()-> new RuntimeException( "Project not Found"));
        existingProject.setEmployeeIds(employeeIds);
        return ProjectMapper.toDto(projectRepository.save(existingProject));
    }
}
    



