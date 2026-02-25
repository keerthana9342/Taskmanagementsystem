package com.example.Taskmanagement.Service;
import java.util.List;
import com.example.Taskmanagement.Model.Task;

public interface Taskservice {
   public Task addtask(Task task);

    public List<Task> getalltask();
    

    public Task gettaskbyid(String id) ;

    public Task updateTask(String id, Task task) ;

    public void deletetask(String id);
    

    public List<Task> getviewbystatus(String status);

}
