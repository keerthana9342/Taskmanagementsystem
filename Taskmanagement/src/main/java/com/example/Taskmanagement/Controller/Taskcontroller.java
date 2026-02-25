package com.example.Taskmanagement.Controller;

import java.util.List;
import com.example.Taskmanagement.Model.Task;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Taskmanagement.Service.Taskservice
;

@RestController
@RequestMapping("/api/task")
public class Taskcontroller {

    private final Taskservice taskservice;

    public Taskcontroller(Taskservice taskservice) {
        this.taskservice = taskservice;
    }

    // Add task
    @PostMapping("/add")
    public Task addtask(@RequestBody Task task) {
        return taskservice.addtask(task);
    }

    // Get all tasks
    @GetMapping("/get")
    public List<Task> getalltask() {   
        return taskservice.getalltask();
    }


    // Get task by id
    @GetMapping("/get/{id}")
    public Task gettaskbyid(@PathVariable String id) {
        return taskservice.gettaskbyid(id);
    }

    // Update task
    @PutMapping("/update/{id}")
    public Task updatetask(@PathVariable String id, @RequestBody Task task) {
        return taskservice.updateTask(id, task);
    }

    // Delete task
    @DeleteMapping("/delete/{id}")
    public void deletetask(@PathVariable String id) {
        taskservice.deletetask(id);
    }

    // Get tasks by status
    @GetMapping("/get/view/{status}")
    public List<Task> getviewbystatus(@PathVariable String status) {
        return taskservice.getviewbystatus(status);
    }
}