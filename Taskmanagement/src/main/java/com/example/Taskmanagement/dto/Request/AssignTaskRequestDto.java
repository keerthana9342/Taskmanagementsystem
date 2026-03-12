package com.example.taskmanagement.dto.Request;
import java.util.List;

import lombok.Data;

@Data
public class AssignTaskRequestDto {

    private List<TaskRequestDto> tasks;
    

}


