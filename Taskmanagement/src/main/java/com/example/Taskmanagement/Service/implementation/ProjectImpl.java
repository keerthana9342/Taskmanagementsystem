package com.example.taskmanagement.Service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.example.taskmanagement.Exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Util.MileStoneMapper;
import com.example.Util.ProjectMapper;
import com.example.Util.SecurityUtil;
import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.Model.MileStone;
import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.Model.Task;
import com.example.taskmanagement.Repository.EmployeeRepository;
import com.example.taskmanagement.Repository.ProjectRepository;
import com.example.taskmanagement.Repository.TaskRepository;
import com.example.taskmanagement.Service.ProjectService;
import com.example.taskmanagement.dto.Request.AssignedEmployeeDto;
import com.example.taskmanagement.dto.Request.MileStoneRequestDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.MileStoneResponseDto;
import com.example.taskmanagement.dto.Response.ProjectResposeDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

@Service
public class ProjectImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public ProjectImpl(ProjectRepository projectRepository,
                       TaskRepository taskRepository,EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    //  Project Methods
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
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
    }

    @Override
    public Project updateProject(String projectId, Project project) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        existingProject.setProjectName(project.getProjectName());
        existingProject.setActive(project.isActive());
        return projectRepository.save(existingProject);
    }

    @Override
    public String deleteProject(String projectId) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        projectRepository.delete(existingProject);
        return "Project deleted successfully";
    }

    @Override
    public ProjectResposeDto assignEmployees(String projectId, List<String> employeeIds) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));
        existingProject.setEmployeeIds(employeeIds);
        Project saved = projectRepository.save(existingProject);
        return ProjectMapper.toDto(saved);
    }

    // Milestone Methods
    @Override
    public MileStoneResponseDto addMilestone(String projectId, MileStoneRequestDto dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        MileStone milestone = new MileStone();
        milestone.setMilestoneId(UUID.randomUUID().toString());
        milestone.setMilestoneName(dto.getMilestoneName());
        milestone.setStartDate(dto.getStartDate());
        milestone.setEndDate(dto.getEndDate());
        milestone.setStatus("PENDING");
        milestone.setEmployeeId(dto.getEmployeeId());
        milestone.setUsername(dto.getUsername());

        if (project.getMileStones() == null) {
            project.setMileStones(new ArrayList<>());
        }
        project.getMileStones().add(milestone);
        projectRepository.save(project);

        return MileStoneMapper.toDto(milestone);
    }

    @Override
    public MileStoneResponseDto updateMilestone(String projectId, String milestoneId, MileStoneRequestDto dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        MileStone milestone = getMilestoneFromProject(project, milestoneId);
        milestone.setMilestoneName(dto.getMilestoneName());
        milestone.setStartDate(dto.getStartDate());
        milestone.setEndDate(dto.getEndDate());
        milestone.setStatus(dto.getStatus());
        milestone.setEmployeeId(dto.getEmployeeId());
        milestone.setUsername(dto.getUsername());

        projectRepository.save(project);
        return MileStoneMapper.toDto(milestone);
    }

    @Override
    public String deleteMilestone(String projectId, String milestoneId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        project.getMileStones().removeIf(m -> m.getMilestoneId().equals(milestoneId));
        projectRepository.save(project);

        // also delete all tasks of this milestone
        taskRepository.deleteByMilestoneId(milestoneId);

        return "Milestone deleted successfully";
    }

    @Override
    public List<MileStoneResponseDto> getMilestonesByProjectId(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        return project.getMileStones().stream()
                .map(MileStoneMapper::toDto)
                .toList();
    }

    @Override
    public MileStoneResponseDto getMilestoneById(String projectId, String milestoneId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        MileStone milestone = getMilestoneFromProject(project, milestoneId);
        return MileStoneMapper.toDto(milestone);
    }

    // Task Methods - now using TaskRepository
    @Override
    public TaskResponseDto addTask(String milestoneId, String projectId, TaskRequestDto dto) {
        // verify milestone exists
        projectRepository.findByMilestoneId(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found: " + milestoneId));
   String loggedInEmail = SecurityUtil.getLoggedInEmail();

    //  Fetch full employee details from DB
    Employee assignedByEmployee = employeeRepository.findByEmail(loggedInEmail)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + loggedInEmail));

    // Build AssignedEmployeeDto for assignedBy
    AssignedEmployeeDto assignedBy = new AssignedEmployeeDto();
    assignedBy.setEmployeeId(assignedByEmployee.getEmployeeId());
    assignedBy.setUsername(assignedByEmployee.getUsername());
    assignedBy.setDesignation(assignedByEmployee.getDesignation());
    assignedBy.setIsActive(assignedByEmployee.getStatus().equals("ACTIVE"));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus("PENDING");
        task.setProjectId(projectId);
        task.setMilestoneId(milestoneId);
        task.setDueDate(dto.getDueDate());
        task.setRemarks(dto.getRemarks());
        task.setIsDeleted(false);
        task.setAssignedTo(dto.getAssignedTo());
        task.setAssignedBy(assignedBy);

        Task saved = taskRepository.save(task);
        return toTaskDto(saved);
    }

    @Override
    public TaskResponseDto updateTask(String taskId, TaskRequestDto dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setDueDate(dto.getDueDate());
        task.setRemarks(dto.getRemarks());
      if (dto.getAssignedTo() != null) {
        //  Fetch logged-in employee from DB
        String loggedInEmail = SecurityUtil.getLoggedInEmail();
        Employee assignedByEmployee = employeeRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + loggedInEmail));

        //  Build AssignedEmployeeDto
        AssignedEmployeeDto assignedBy = new AssignedEmployeeDto();
        assignedBy.setEmployeeId(assignedByEmployee.getEmployeeId());
        assignedBy.setUsername(assignedByEmployee.getUsername());
        assignedBy.setDesignation(assignedByEmployee.getDesignation());
        assignedBy.setIsActive(assignedByEmployee.getStatus().equals("ACTIVE"));

        task.setAssignedTo(dto.getAssignedTo());
        task.setAssignedBy(assignedBy); 
    }

        return toTaskDto(taskRepository.save(task));
    }

    @Override
    public String deleteTask(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        task.setIsDeleted(true);
        taskRepository.save(task);
        return "Task deleted successfully";
    }

    @Override
    public List<TaskResponseDto> getTasksByMilestoneId(String milestoneId) {
        return taskRepository.findByMilestoneId(milestoneId)
                .stream()
                .map(this::toTaskDto)
                .toList();
    }

    //  Helper Methods
    private MileStone getMilestoneFromProject(Project project, String milestoneId) {
        return project.getMileStones().stream()
                .filter(m -> m.getMilestoneId().equals(milestoneId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found: " + milestoneId));
    }

    private TaskResponseDto toTaskDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setProjectId(task.getProjectId());
        dto.setMilestoneId(task.getMilestoneId());
        dto.setDueDate(task.getDueDate());
        dto.setCompletedDate(task.getCompletedDate());
        dto.setRemarks(task.getRemarks());
        dto.setIsDeleted(task.getIsDeleted());
        dto.setAssignedTo(task.getAssignedTo());    
        dto.setAssignedBy(task.getAssignedBy());
        return dto;
    }
}