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
import com.example.taskmanagement.dto.Request.AssignedEmployeeDto;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

@Service
public class TaskserviceImpl implements TaskService {

    private final TaskRepository taskrepo;
    private final EmployeeRepository employeeRepository;

    public TaskserviceImpl(TaskRepository taskrepo, EmployeeRepository employeeRepository) {
        this.taskrepo = taskrepo;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<TaskResponseDto> getAllTasks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> taskPage = taskrepo.findAll(pageable);

        List<TaskResponseDto> responseList = new ArrayList<>();
        for (Task task : taskPage.getContent()) {
            responseList.add(TaskMapper.toDTO(task));
        }
        return responseList;
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

        // ✅ If COMPLETED
        if ("COMPLETED".equalsIgnoreCase(status)) {
            task.setStatus("COMPLETED");
            task.setCompletedDate(LocalDateTime.now());
            task.setRemarks(null);

        // ✅ If OVERDUE - dueDate passed and not completed
        } else if (task.getDueDate().isBefore(LocalDateTime.now())) {
            task.setStatus("OVERDUE");
            task.setCompletedDate(null);
            if (remarks != null) {
                task.setRemarks(remarks);
            }

        // ✅ If PENDING or IN_PROGRESS
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
