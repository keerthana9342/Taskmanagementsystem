package com.example.Taskmanagement.Service;
import java.util.List;

import com.example.Taskmanagement.Model.Task;
import com.example.Taskmanagement.Model.TaskStatus;
import com.example.Taskmanagement.dto.TaskrequestDto;
import com.example.Taskmanagement.dto.TaskresponseDto;

public interface Taskservice {
    
   public TaskresponseDto addtask(TaskrequestDto task);

    public List<TaskresponseDto> getalltask();
    

    public TaskresponseDto gettaskbyid(String id) ;

    public TaskresponseDto updateTask(String id, TaskrequestDto dto);

    public void deletetask(String id);
    

    //public List<TaskresponseDto> getviewbystatus1(TaskStatus status);

    List<TaskresponseDto> getviewbystatus(TaskStatus status);

}
