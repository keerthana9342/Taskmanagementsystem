package com.example.taskmanagement.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.Service.ProjectService;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/add")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.addProject(project));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable String projectId,
                                                 @RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProject(projectId, project));
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable String projectId) {
        return ResponseEntity.ok(projectService.deleteProject(projectId));
    }
    @PutMapping("/{projectId}/assign-employees")
public ResponseEntity<?> assignEmployees(
        @PathVariable String projectId,
        @RequestBody List<String> employeeIds) {

    return ResponseEntity.ok(projectService.assignEmployees(projectId, employeeIds));
}
}


