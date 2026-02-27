package com.example.Taskmanagement.Controller;

import java.util.List;
import com.example.Taskmanagement.Model.Task;
import com.example.Taskmanagement.Model.TaskStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Taskmanagement.Service.Taskservice;
import com.example.Taskmanagement.dto.TaskrequestDto;
import com.example.Taskmanagement.dto.TaskresponseDto;



@RestController
@RequestMapping("/api/task")
public class Taskcontroller {

    private final Taskservice taskservice;

    public Taskcontroller(Taskservice taskservice) {
        this.taskservice = taskservice;
    }

    // Add Task
    @PostMapping("/add")
    public ResponseEntity<TaskresponseDto> addTask(@RequestBody TaskrequestDto dto) {
    return ResponseEntity.ok(taskservice.addtask(dto));
}

    // Get All
    @GetMapping("/get")
    public List<TaskresponseDto> getalltask() {
        return taskservice.getalltask();
    }

    // Get By ID
    @GetMapping("/get/{id}")
    public TaskresponseDto gettaskbyid(@PathVariable String id) {
        return taskservice.gettaskbyid(id);
    }

    // Update
    @PutMapping("/update/{id}")
    public TaskresponseDto updatetask(@PathVariable String id,
                                      @RequestBody TaskrequestDto dto) {
        return taskservice.updateTask(id, dto);
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public void deletetask(@PathVariable String id) {
        taskservice.deletetask(id);
    }

    // Get By Status
    @GetMapping("/get/view/{status}")
    public List<TaskresponseDto> getviewbystatus(@PathVariable TaskStatus status) {
        return taskservice.getviewbystatus(status);
    }
}