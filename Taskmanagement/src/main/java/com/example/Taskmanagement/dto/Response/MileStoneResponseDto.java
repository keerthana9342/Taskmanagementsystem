package com.example.taskmanagement.dto.Response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MileStoneResponseDto {
    private String milestoneId;
    private String milestoneName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String employeeId;
    private String username;
    private List<TaskResponseDto> tasks;
}


