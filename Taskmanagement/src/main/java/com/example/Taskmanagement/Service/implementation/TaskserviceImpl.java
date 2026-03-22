package com.example.taskmanagement.Service.implementation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.Util.TaskMapper;
import com.example.taskmanagement.Exception.ResourceNotFoundException;
import com.example.taskmanagement.Model.Task;
import com.example.taskmanagement.Repository.EmployeeRepository;
import com.example.taskmanagement.Repository.TaskRepository;
import com.example.taskmanagement.Service.TaskService;
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
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        task.setDueDate(dto.getDueDate());
        task.setEmployeeId(dto.getEmployeeId());
        task.setProjectId(dto.getProjectId());
        task.setIsDeleted(false);

        Task savedTask = taskrepo.save(task);
        return TaskMapper.toDTO(savedTask);
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

        if (dto.getEmployeeId() != null) {
            existingTask.setEmployeeId(dto.getEmployeeId());
        }
        if (dto.getProjectId() != null) {
            existingTask.setProjectId(dto.getProjectId());
        }

        Task updatedTask = taskrepo.save(existingTask);
        return TaskMapper.toDTO(updatedTask);
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
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus("COMPLETED");
        task.setCompletedDate(LocalDateTime.now());

        return TaskMapper.toDTO(taskrepo.save(task));
    }

    @Override
    public TaskResponseDto updateTaskStatus(String taskId, String status, String remarks) {
        Task task = taskrepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);

        // If overdue
        if (!"COMPLETED".equalsIgnoreCase(status)
                && task.getDueDate().isBefore(LocalDateTime.now())) {
            task.setStatus("OVERDUE");
            task.setRemarks(remarks);
        }

        // If completed
        if ("COMPLETED".equalsIgnoreCase(status)) {
            task.setCompletedDate(LocalDateTime.now());
        }

        if (remarks != null) {
            task.setRemarks(remarks);
        }

        return TaskMapper.toDTO(taskrepo.save(task));
    }
}