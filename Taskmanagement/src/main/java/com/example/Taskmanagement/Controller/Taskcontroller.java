package com.example.taskmanagement.Controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanagement.Model.TaskStatus;
import com.example.taskmanagement.Service.TaskService;
import com.example.taskmanagement.dto.APIresponse;
import com.example.taskmanagement.dto.Request.TaskRequestDto;
import com.example.taskmanagement.dto.Response.TaskResponseDto;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/task")
public class Taskcontroller {

    private final TaskService taskservice;

    public Taskcontroller(TaskService taskservice) {
        this.taskservice = taskservice;
    }

    @PostMapping("/add")
    public ResponseEntity<APIresponse<TaskResponseDto>> addTask(@Valid @RequestBody TaskRequestDto dto) {

        TaskResponseDto savedTask = taskservice.addTask(dto);

        APIresponse<TaskResponseDto> response =
                new APIresponse<>("SUCCESS","Task added successfully",savedTask,LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<APIresponse<List<TaskResponseDto>>> getalltask(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size){

        List<TaskResponseDto> tasks = taskservice.getAllTasks(page,size);

        APIresponse<List<TaskResponseDto>> response =
                new APIresponse<>("SUCCESS","All tasks fetched",tasks,LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<APIresponse<TaskResponseDto>> gettaskbyid(@PathVariable String id){

        TaskResponseDto task = taskservice.getTaskById(id);

        APIresponse<TaskResponseDto> response =
                new APIresponse<>("SUCCESS","Task fetched",task,LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIresponse<TaskResponseDto>> updatetask(
            @PathVariable String id,
            @Valid @RequestBody TaskRequestDto dto){

        TaskResponseDto updated = taskservice.updateTask(id,dto);

        APIresponse<TaskResponseDto> response =
                new APIresponse<>("SUCCESS","Task updated",updated,LocalDateTime.now());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
public ResponseEntity<APIresponse<String>> deletetask(@PathVariable String id){

    String message = taskservice.deleteTask(id);

    APIresponse<String> response =
            new APIresponse<>("SUCCESS","Task deleted",message,LocalDateTime.now());

    return ResponseEntity.ok(response);
}

    @GetMapping("/get/view/{status}")
    public ResponseEntity<APIresponse<List<TaskResponseDto>>> getviewByStatus(@PathVariable TaskStatus status) {

        List<TaskResponseDto> tasks = taskservice.getViewByStatus(status);

        APIresponse<List<TaskResponseDto>> response =
            new APIresponse<>("SUCCESS", "Tasks fetched by status", tasks, LocalDateTime.now());

        return ResponseEntity.ok(response);
}
}