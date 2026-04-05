package com.example.taskmanagement.Controller;

import java.util.List;

import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Add;
import org.springframework.expression.spel.ast.Assign;
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
import com.example.taskmanagement.dto.Request.MileStoneRequestDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.MileStoneResponseDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;


import jakarta.validation.Valid;

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
 @PostMapping("/{projectId}/milestones")
    public ResponseEntity<MileStoneResponseDto> addMilestone(
            @PathVariable String projectId,
            @Valid @RequestBody MileStoneRequestDto dto) {
        return ResponseEntity.ok(projectService.addMilestone(projectId, dto));
    }

    @PutMapping("/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<MileStoneResponseDto> updateMilestone(
            @PathVariable String projectId,
            @PathVariable String milestoneId,
            @Valid @RequestBody MileStoneRequestDto dto) {
        return ResponseEntity.ok(projectService.updateMilestone(projectId, milestoneId, dto));
    }

    @DeleteMapping("/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<String> deleteMilestone(
            @PathVariable String projectId,
            @PathVariable String milestoneId) {
        return ResponseEntity.ok(projectService.deleteMilestone(projectId, milestoneId));
    }

    @GetMapping("/{projectId}/milestones")
    public ResponseEntity<List<MileStoneResponseDto>> getMilestones(
            @PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getMilestonesByProjectId(projectId));
    }

    @GetMapping("/{projectId}/milestones/{milestoneId}")
    public ResponseEntity<MileStoneResponseDto> getMilestoneById(
            @PathVariable String projectId,
            @PathVariable String milestoneId) {
        return ResponseEntity.ok(projectService.getMilestoneById(projectId, milestoneId));
    }

    // ✅ Task Endpoints
// ✅ Task Endpoints
@PostMapping("/{projectId}/milestones/{milestoneId}/tasks")
public ResponseEntity<TaskResponseDto> addTask(
        @PathVariable String projectId,
        @PathVariable String milestoneId,
        @Valid @RequestBody TaskRequestDto dto) {
    return ResponseEntity.ok(projectService.addTask(milestoneId, projectId, dto));
}

@PutMapping("/tasks/{taskId}")
public ResponseEntity<TaskResponseDto> updateTask(
        @PathVariable String taskId,
        @Valid @RequestBody TaskRequestDto dto) {
    return ResponseEntity.ok(projectService.updateTask(taskId, dto));
}

@DeleteMapping("/tasks/{taskId}")
public ResponseEntity<String> deleteTask(@PathVariable String taskId) {
    return ResponseEntity.ok(projectService.deleteTask(taskId));
}

@GetMapping("/milestones/{milestoneId}/tasks")
public ResponseEntity<List<TaskResponseDto>> getTasks(
        @PathVariable String milestoneId) {
    return ResponseEntity.ok(projectService.getTasksByMilestoneId(milestoneId));
}
}