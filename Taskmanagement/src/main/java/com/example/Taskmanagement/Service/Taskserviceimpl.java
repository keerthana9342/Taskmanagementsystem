package com.example.Taskmanagement.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Taskmanagement.Model.Task;
import com.example.Taskmanagement.Repository.Taskrepo;


@Service
public class Taskserviceimpl implements Taskservice {

    @Autowired
    private Taskrepo taskrepo;

    // Get all tasks
    public List<Task> getalltask() {
        return taskrepo.findAll();
    }

    // Add task
    public Task addtask(Task task) {
        return taskrepo.save(task);
    }

    // Get task by id
    public Task gettaskbyid(String id) {
        return taskrepo.findById(id).orElse(null);
    }

    // Update task
    public Task updateTask(String id, Task task) {
        Task existingTask = taskrepo.findById(id).orElse(null);

        if (existingTask != null) {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            existingTask.setDuedate(task.getDuedate());
            return taskrepo.save(existingTask);
        }

        throw new RuntimeException("Task not found with id: " + id);
    }

    // Delete task
    
    public void deletetask(String id) {
        taskrepo.deleteById(id);
    }

    // Get tasks by status
    public List<Task> getviewbystatus(String status) {
        return taskrepo.findByStatus(status);
    }
}
