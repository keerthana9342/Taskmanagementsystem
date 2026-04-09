package com.example.taskmanagement.Service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.example.Util.MileStoneMapper;
import com.example.Util.ProjectMapper;
import com.example.Util.SecurityUtil;
import com.example.taskmanagement.Exception.ResourceNotFoundException;
import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.Model.MileStone;
import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.Model.Task;
import com.example.taskmanagement.Repository.EmployeeRepository;
import com.example.taskmanagement.Repository.ProjectRepository;
import com.example.taskmanagement.Repository.TaskRepository;
import com.example.taskmanagement.Service.ProjectService;
import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.AssignedEmployeeDto;
import com.example.taskmanagement.dto.Request.MileStoneRequestDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.EmployeeProjectSummaryResponseDto;
import com.example.taskmanagement.dto.Response.MileStoneResponseDto;
import com.example.taskmanagement.dto.Response.ProjectResposeDto;
import com.example.taskmanagement.dto.Response.ProjectTaskResponseDto;
import com.example.taskmanagement.dto.Response.ProjectWithTasksDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class ProjectImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final MongoTemplate mongoTemplate;

    public ProjectImpl(ProjectRepository projectRepository,
                       TaskRepository taskRepository,
                       EmployeeRepository employeeRepository,
                       MongoTemplate mongoTemplate) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.mongoTemplate = mongoTemplate;

    }

    // ==================== PROJECT ====================

    @Override
    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

  @Override
public PageResponse<ProjectResposeDto> getAllProjects(
        String keyword,
        String status,
        String employeeId,
        String milestoneId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        int page,
        int size) {

    Pageable pageable = PageRequest.of(page, size);
    Query query = new Query();
    List<Criteria> criteriaList = new ArrayList<>();

    //  Search keyword
    if (keyword != null && !keyword.trim().isEmpty()) {
        criteriaList.add(new Criteria().orOperator(
                Criteria.where("projectId").regex(keyword, "i"),
                Criteria.where("projectName").regex(keyword, "i"),
                Criteria.where("employeeIds").regex(keyword, "i"),
                Criteria.where("mileStones.milestoneId").regex(keyword, "i"),
                Criteria.where("mileStones.milestoneName").regex(keyword, "i"),
                Criteria.where("mileStones.status").regex(keyword, "i"),
                Criteria.where("mileStones.employeeId").regex(keyword, "i"),
                Criteria.where("mileStones.username").regex(keyword, "i")
        ));
    }

    //  Filters
    if (status != null && !status.isEmpty()) {
        criteriaList.add(Criteria.where("mileStones.status").is(status));
    }
    if (employeeId != null && !employeeId.isEmpty()) {
        criteriaList.add(Criteria.where("employeeIds").in(employeeId));
    }
    if (milestoneId != null && !milestoneId.isEmpty()) {
        criteriaList.add(Criteria.where("mileStones.milestoneId").is(milestoneId));
    }
    if (startDate != null && endDate != null) {
        criteriaList.add(Criteria.where("mileStones.startDate")
                .gte(startDate).lte(endDate));
    }

    //  Combine all criteria
    if (!criteriaList.isEmpty()) {
        query.addCriteria(new Criteria().andOperator(
                criteriaList.toArray(new Criteria[0])));
    }

    //  Get total count
    long total = mongoTemplate.count(query, Project.class);

    //  Apply pagination
    query.with(pageable);
    List<Project> projects = mongoTemplate.find(query, Project.class);

    List<ProjectResposeDto> projectDtos = projects.stream()
            .map(ProjectMapper::toDto)
            .toList();

    return new PageResponse<>(
            projectDtos,
            page,
            size,
            total,
            (int) Math.ceil((double) total / size)
    );
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

    // ==================== MILESTONE ====================

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

    // ==================== TASK ====================

    @Override
    public TaskResponseDto addTask(String milestoneId, String projectId, TaskRequestDto dto) {
        projectRepository.findByMilestoneId(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found: " + milestoneId));

        String loggedInEmail = SecurityUtil.getLoggedInEmail();
        Employee assignedByEmployee = employeeRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + loggedInEmail));

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
            String loggedInEmail = SecurityUtil.getLoggedInEmail();
            Employee assignedByEmployee = employeeRepository.findByEmail(loggedInEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + loggedInEmail));

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

    // ==================== FULL DETAIL ====================

    @Override
    public ProjectWithTasksDto getProjectFullDetail(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        ProjectWithTasksDto result = new ProjectWithTasksDto();
        result.setProjectId(project.getProjectId());
        result.setProjectName(project.getProjectName());
        result.setActive(project.isActive());
        result.setEmployeeIds(project.getEmployeeIds());

        List<MileStoneResponseDto> milestoneWithTasks = project.getMileStones()
                .stream()
                .map(milestone -> {
                    MileStoneResponseDto m = new MileStoneResponseDto();
                    m.setMilestoneId(milestone.getMilestoneId());
                    m.setMilestoneName(milestone.getMilestoneName());
                    m.setStartDate(milestone.getStartDate());
                    m.setEndDate(milestone.getEndDate());
                    m.setStatus(milestone.getStatus());
                    m.setEmployeeId(milestone.getEmployeeId());
                    m.setUsername(milestone.getUsername());

                    List<TaskResponseDto> tasks = taskRepository
                            .findByMilestoneId(milestone.getMilestoneId())
                            .stream()
                            .map(this::toTaskDto)
                            .toList();
                    m.setTasks(tasks);
                    return m;
                })
                .toList();

        result.setMileStones(milestoneWithTasks);
        return result;
    }

    // ==================== EMPLOYEE TASK SUMMARY ====================

    @Override
    public EmployeeProjectSummaryResponseDto getEmployeeTaskSummary(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + employeeId));

        List<Task> allTasks = taskRepository.findByAssignedToEmployeeId(employeeId);

        Map<String, List<Task>> tasksByProject = allTasks.stream()
                .collect(Collectors.groupingBy(Task::getProjectId));

        List<ProjectTaskResponseDto> projects = tasksByProject.entrySet().stream()
                .map(entry -> {
                    String projectId = entry.getKey();
                    List<Task> projectTasks = entry.getValue();

                    // skip if project not found
                    Optional<Project> projectOpt = projectRepository.findById(projectId);
                    if (projectOpt.isEmpty()) return null;
                    Project project = projectOpt.get();

                    ProjectTaskResponseDto projectDto = new ProjectTaskResponseDto();
                    projectDto.setProjectId(project.getProjectId());
                    projectDto.setProjectName(project.getProjectName());

                    Map<String, List<Task>> tasksByMilestone = projectTasks.stream()
                            .collect(Collectors.groupingBy(Task::getMilestoneId));

                    List<MileStoneResponseDto> milestones = tasksByMilestone.entrySet().stream()
                            .map(milestoneEntry -> {
                                String milestoneId = milestoneEntry.getKey();
                                List<Task> milestoneTasks = milestoneEntry.getValue();

                                //  skip if milestone not found
                                Optional<MileStone> milestoneOpt = project.getMileStones().stream()
                                        .filter(m -> m.getMilestoneId().equals(milestoneId))
                                        .findFirst();
                                if (milestoneOpt.isEmpty()) return null;
                                MileStone milestone = milestoneOpt.get();

                                MileStoneResponseDto milestoneDto = new MileStoneResponseDto();
                                milestoneDto.setMilestoneId(milestone.getMilestoneId());
                                milestoneDto.setMilestoneName(milestone.getMilestoneName());
                                milestoneDto.setTasks(milestoneTasks.stream()
                                        .map(this::toSimpleTaskDto)
                                        .toList());
                                return milestoneDto;
                            })
                            .filter(m -> m != null) //  remove nulls
                            .toList();

                    projectDto.setMilestones(milestones);
                    return projectDto;
                })
                .filter(p -> p != null) //  remove nulls
                .toList();

        EmployeeProjectSummaryResponseDto summary = new EmployeeProjectSummaryResponseDto();
        summary.setEmployeeId(employee.getEmployeeId());
        summary.setUsername(employee.getUsername());
        summary.setDesignation(employee.getDesignation());
        summary.setProjects(projects);

        return summary;
    }

    // ==================== HELPERS ====================

    private MileStone getMilestoneFromProject(Project project, String milestoneId) {
        return project.getMileStones().stream()
                .filter(m -> m.getMilestoneId().equals(milestoneId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Milestone not found: " + milestoneId));
    }

    //  full toTaskDto — includes assignedTo with full project summary
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
        dto.setAssignedBy(task.getAssignedBy());

        if (task.getAssignedTo() != null) {
            EmployeeProjectSummaryResponseDto assignedToDetail = new EmployeeProjectSummaryResponseDto();
            assignedToDetail.setEmployeeId(task.getAssignedTo().getEmployeeId());
            assignedToDetail.setUsername(task.getAssignedTo().getUsername());
            assignedToDetail.setDesignation(task.getAssignedTo().getDesignation());

            List<Task> allTasks = taskRepository.findByAssignedToEmployeeId(
                    task.getAssignedTo().getEmployeeId());

            Map<String, List<Task>> tasksByProject = allTasks.stream()
                    .collect(Collectors.groupingBy(Task::getProjectId));

            List<ProjectTaskResponseDto> projects = tasksByProject.entrySet().stream()
                    .map(entry -> {
                        String projectId = entry.getKey();
                        List<Task> projectTasks = entry.getValue();

                        //  skip if project not found
                        Optional<Project> projectOpt = projectRepository.findById(projectId);
                        if (projectOpt.isEmpty()) return null;
                        Project project = projectOpt.get();

                        ProjectTaskResponseDto projectDto = new ProjectTaskResponseDto();
                        projectDto.setProjectId(project.getProjectId());
                        projectDto.setProjectName(project.getProjectName());

                        Map<String, List<Task>> tasksByMilestone = projectTasks.stream()
                                .collect(Collectors.groupingBy(Task::getMilestoneId));

                        List<MileStoneResponseDto> milestones = tasksByMilestone.entrySet().stream()
                                .map(milestoneEntry -> {
                                    String milestoneId = milestoneEntry.getKey();
                                    List<Task> milestoneTasks = milestoneEntry.getValue();

                                    // skip if milestone not found
                                    Optional<MileStone> milestoneOpt = project.getMileStones().stream()
                                            .filter(m -> m.getMilestoneId().equals(milestoneId))
                                            .findFirst();
                                    if (milestoneOpt.isEmpty()) return null;
                                    MileStone milestone = milestoneOpt.get();

                                    MileStoneResponseDto milestoneDto = new MileStoneResponseDto();
                                    milestoneDto.setMilestoneId(milestone.getMilestoneId());
                                    milestoneDto.setMilestoneName(milestone.getMilestoneName());
                                    milestoneDto.setTasks(milestoneTasks.stream()
                                            .map(this::toSimpleTaskDto) //  avoids infinite loop
                                            .toList());
                                    return milestoneDto;
                                })
                                .filter(m -> m != null) //  remove nulls
                                .toList();

                        projectDto.setMilestones(milestones);
                        return projectDto;
                    })
                    .filter(p -> p != null) // remove nulls
                    .toList();

            assignedToDetail.setProjects(projects);
            dto.setAssignedTo(assignedToDetail);
        }

        return dto;
    }

    //  simple toTaskDto — no assignedTo details, avoids infinite loop
    private TaskResponseDto toSimpleTaskDto(Task task) {
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
        dto.setAssignedBy(task.getAssignedBy());
        //  no assignedTo — avoids infinite loop
        return dto;
    }
    @Override
public PageResponse<Project> searchProjects(String keyword, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Project> projectPage;

    if (keyword == null || keyword.trim().isEmpty()) {
        projectPage = projectRepository.findAll(pageable);
    } else {
        projectPage = projectRepository.searchProjects(keyword.trim(), pageable);
    }

    return new PageResponse<>(
            projectPage.getContent(),
            projectPage.getNumber(),
            projectPage.getSize(),
            projectPage.getTotalElements(),
            projectPage.getTotalPages()
    );
}
}