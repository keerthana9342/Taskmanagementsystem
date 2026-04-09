package com.example.taskmanagement.Controller;

import java.time.LocalDateTime;
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
import com.example.taskmanagement.dto.APIresponse;
import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.MileStoneRequestDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.EmployeeProjectSummaryResponseDto;
import com.example.taskmanagement.dto.Response.MileStoneResponseDto;
import com.example.taskmanagement.dto.Response.ProjectResposeDto;
import com.example.taskmanagement.dto.Response.ProjectWithTasksDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

@GetMapping("/get")
public ResponseEntity<APIresponse<PageResponse<ProjectResposeDto>>> getAllProjects(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String employeeId,
        @RequestParam(required = false) String milestoneId,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) {

    LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : null;
    LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : null;

    PageResponse<ProjectResposeDto> projects = projectService.getAllProjects(
            keyword, status, employeeId, milestoneId, start, end, page, size);

    return ResponseEntity.ok(new APIresponse<>("SUCCESS",
            "Projects fetched successfully", projects, LocalDateTime.now()));
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

    // Task Endpoints

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
 @GetMapping("/{projectId}/full")
    //  returns project + all milestones + all tasks in one response
    public ResponseEntity<ProjectWithTasksDto> getProjectFullDetail(
            @PathVariable String projectId) {
        return ResponseEntity.ok(projectService.getProjectFullDetail(projectId));
    }
    //  Get all tasks assigned to an employee across all projects
@GetMapping("/employees/{employeeId}/tasks")
public ResponseEntity<EmployeeProjectSummaryResponseDto> getEmployeeTaskSummary(
        @PathVariable String employeeId) {
    return ResponseEntity.ok(projectService.getEmployeeTaskSummary(employeeId));
}

}