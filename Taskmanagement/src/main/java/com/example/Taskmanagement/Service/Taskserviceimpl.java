package com.example.Taskmanagement.Service;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;

import com.example.Taskmanagement.Exception.ResourceNotFoundException;
import com.example.Taskmanagement.Model.Task;
import com.example.Taskmanagement.Model.TaskStatus;
import com.example.Taskmanagement.Repository.Taskrepo;
import com.example.Taskmanagement.dto.TaskrequestDto;
import com.example.Taskmanagement.dto.TaskresponseDto;
import com.example.Util.TaskMapper;

import lombok.RequiredArgsConstructor;

@Service
public class Taskserviceimpl implements Taskservice {

    public final Taskrepo taskrepo;

    // Constructor Injection
    public Taskserviceimpl(Taskrepo taskrepo) {
        this.taskrepo = taskrepo;
    }


    @Override
    public List<TaskresponseDto> getalltask() {

        List<Task> taskList = taskrepo.findAll();
        List<TaskresponseDto> responseList = new ArrayList<>();

        for (Task task : taskList) {
            responseList.add(TaskMapper.toDTO(task));
        }

        return responseList;
    }

    
    @Override
    public TaskresponseDto addtask(TaskrequestDto dto) {

        Task task = TaskMapper.toEntity(dto);
        Task savedTask = taskrepo.save(task);

        return TaskMapper.toDTO(savedTask);
    }

    //  Get Task By ID
    @Override
    public TaskresponseDto gettaskbyid(String id) {

        Task task = taskrepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + id));

        return TaskMapper.toDTO(task);
    }

    //  Delete Task
    @Override
    public void deletetask(String id) {

        Task task = taskrepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + id));

        taskrepo.delete(task);
    }

    // Update Task
    public TaskresponseDto updateTask(String id, TaskrequestDto dto) {

        Task existingTask = taskrepo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + id));

        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setStatus(dto.getStatus());
        existingTask.setDueDate(dto.getDueDate());

        Task updatedTask = taskrepo.save(existingTask);

        return TaskMapper.toDTO(updatedTask);
    }

    

    @Override
public List<TaskresponseDto> getviewbystatus(TaskStatus status) {

    System.out.println("Status received: " + status);

    List<Task> tasks = taskrepo.findByStatus(status);

    System.out.println("Tasks found: " + tasks.size());

    return tasks.stream().map(task -> {
        TaskresponseDto dto = new TaskresponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        return dto;
    }).toList();
}
    
}