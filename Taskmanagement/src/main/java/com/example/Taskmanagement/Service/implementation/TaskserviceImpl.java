package com.example.taskmanagement.Service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.Util.SecurityUtil;
import com.example.Util.TaskMapper;
import com.example.taskmanagement.Exception.ResourceNotFoundException;
import com.example.taskmanagement.Model.Employee;
import com.example.taskmanagement.Model.Task;
import com.example.taskmanagement.Repository.EmployeeRepository;
import com.example.taskmanagement.Repository.TaskRepository;
import com.example.taskmanagement.Service.TaskService;
import com.example.taskmanagement.dto.PageResponse;
import com.example.taskmanagement.dto.Request.AssignedEmployeeDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Service
public class TaskserviceImpl implements TaskService {

    private final TaskRepository taskrepo;
    private final EmployeeRepository employeeRepository;
    private final MongoTemplate mongoTemplate;

    public TaskserviceImpl(TaskRepository taskrepo, EmployeeRepository employeeRepository,MongoTemplate mongoTemplate) {
        this.taskrepo = taskrepo;
        this.employeeRepository = employeeRepository;
        this.mongoTemplate = mongoTemplate;
    }

@Override
public PageResponse<TaskResponseDto> getAllTasks(
        String keyword,
        String status,
        String projectId,
        String milestoneId,
        String employeeId,
        String designation,
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
                Criteria.where("title").regex(keyword, "i"),
                Criteria.where("description").regex(keyword, "i"),
                Criteria.where("status").regex(keyword, "i"),
                Criteria.where("projectId").regex(keyword, "i"),
                Criteria.where("milestoneId").regex(keyword, "i"),
                Criteria.where("remarks").regex(keyword, "i"),
                Criteria.where("assignedTo.employeeId").regex(keyword, "i"),
                Criteria.where("assignedTo.username").regex(keyword, "i"),
                Criteria.where("assignedTo.designation").regex(keyword, "i"),
                Criteria.where("assignedBy.employeeId").regex(keyword, "i"),
                Criteria.where("assignedBy.username").regex(keyword, "i")
        ));
    }

    //  Filters
    if (status != null && !status.isEmpty()) {
        criteriaList.add(Criteria.where("status").is(status));
    }
    if (projectId != null && !projectId.isEmpty()) {
        criteriaList.add(Criteria.where("projectId").is(projectId));
    }
    if (milestoneId != null && !milestoneId.isEmpty()) {
        criteriaList.add(Criteria.where("milestoneId").is(milestoneId));
    }
    if (employeeId != null && !employeeId.isEmpty()) {
        criteriaList.add(Criteria.where("assignedTo.employeeId").is(employeeId));
    }
    if (designation != null && !designation.isEmpty()) {
        criteriaList.add(Criteria.where("assignedTo.designation").is(designation));
    }
    if (startDate != null && endDate != null) {
        criteriaList.add(Criteria.where("dueDate").gte(startDate).lte(endDate));
    }

    // Combine all criteria
    if (!criteriaList.isEmpty()) {
        query.addCriteria(new Criteria().andOperator(
                criteriaList.toArray(new Criteria[0])));
    }

    //  Get total count
    long total = mongoTemplate.count(query, Task.class);

    //  Apply pagination
    query.with(pageable);
    List<Task> tasks = mongoTemplate.find(query, Task.class);

    List<TaskResponseDto> taskDtos = tasks.stream()
            .map(TaskMapper::toDTO)
            .toList();

    return new PageResponse<>(
            taskDtos,
            page,
            size,
            total,
            (int) Math.ceil((double) total / size)
    );
}
    @Override
    public TaskResponseDto addTask(TaskRequestDto dto) {

        //  Get logged in user email from token
        String loggedInEmail = SecurityUtil.getLoggedInEmail();
        Employee loggedInEmployee = employeeRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + loggedInEmail));

        //  Build assignedBy from token
        AssignedEmployeeDto assignedBy = new AssignedEmployeeDto();
        assignedBy.setEmployeeId(loggedInEmployee.getEmployeeId());
        assignedBy.setUsername(loggedInEmployee.getUsername());
        assignedBy.setIsActive(loggedInEmployee.getStatus().equalsIgnoreCase("ACTIVE"));
        assignedBy.setDesignation(loggedInEmployee.getDesignation());

        //  Build assignedTo from request body
        AssignedEmployeeDto assignedTo = new AssignedEmployeeDto();
        assignedTo.setEmployeeId(dto.getAssignedTo().getEmployeeId());
        assignedTo.setUsername(dto.getAssignedTo().getUsername());
        assignedTo.setIsActive(dto.getAssignedTo().getIsActive());
        assignedTo.setDesignation(dto.getAssignedTo().getDesignation());

        //  Build task
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        task.setDueDate(dto.getDueDate());
        task.setProjectId(dto.getProjectId());
        task.setMilestoneId(dto.getMilestoneId());
        task.setRemarks(dto.getRemarks());
        task.setAssignedTo(assignedTo);    //  fixed
        task.setAssignedBy(assignedBy);    // fixed
        task.setIsDeleted(false);

        return TaskMapper.toDTO(taskrepo.save(task));
    }

    @Override
    public TaskResponseDto getTaskById(String id) {
        Task task = taskrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return TaskMapper.toDTO(task);
    }

    @Override
    public String deleteTask(String id) {
        Task task = taskrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskrepo.delete(task);
        return "Task deleted successfully";
    }

    @Override
    public TaskResponseDto updateTask(String id, TaskRequestDto dto) {
        Task existingTask = taskrepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setStatus(dto.getStatus());
        existingTask.setDueDate(dto.getDueDate());

        //  Update assignedTo if given
        if (dto.getAssignedTo() != null) {
            AssignedEmployeeDto assignedTo = new AssignedEmployeeDto();
            assignedTo.setEmployeeId(dto.getAssignedTo().getEmployeeId());
            assignedTo.setUsername(dto.getAssignedTo().getUsername());
            assignedTo.setIsActive(dto.getAssignedTo().getIsActive());
            assignedTo.setDesignation(dto.getAssignedTo().getDesignation());
            existingTask.setAssignedTo(assignedTo);
        }
        if (dto.getProjectId() != null) {
            existingTask.setProjectId(dto.getProjectId());
        }
        if (dto.getMilestoneId() != null) {
            existingTask.setMilestoneId(dto.getMilestoneId());
        }

        return TaskMapper.toDTO(taskrepo.save(existingTask));
    }

    @Override
    public List<TaskResponseDto> getviewByStatus(String status) {
        List<Task> tasks = taskrepo.findByStatus(status);
        return tasks.stream()
                .map(TaskMapper::toDTO)
                .toList();
    }

    @Override
    public TaskResponseDto completeTask(String taskId) {
        Task task = taskrepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        task.setStatus("COMPLETED");
        task.setCompletedDate(LocalDateTime.now());

        return TaskMapper.toDTO(taskrepo.save(task));
    }

    @Override
    public TaskResponseDto updateTaskStatus(String taskId, String status, String remarks) {
        Task task = taskrepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));

        //  If COMPLETED
        if ("COMPLETED".equalsIgnoreCase(status)) {
            task.setStatus("COMPLETED");
            task.setCompletedDate(LocalDateTime.now());
            task.setRemarks(null);

        // If OVERDUE - dueDate passed and not completed
        } else if (task.getDueDate().isBefore(LocalDateTime.now())) {
            task.setStatus("OVERDUE");
            task.setCompletedDate(null);
            if (remarks != null) {
                task.setRemarks(remarks);
            }

        //  If PENDING or IN_PROGRESS
        } else {
            task.setStatus(status);
            task.setCompletedDate(null);
            if (remarks != null) {
                task.setRemarks(remarks);
            }
        }

        return TaskMapper.toDTO(taskrepo.save(task));
    }

}
