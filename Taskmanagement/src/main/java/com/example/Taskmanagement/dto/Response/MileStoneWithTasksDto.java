package com.example.taskmanagement.dto.Response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class MileStoneWithTasksDto {
    private String milestoneId;     // milestone id
    private String milestoneName;   // milestone name
    private String startDate;       // start date
    private String endDate;         // end date
    private String status;          // PENDING, COMPLETED etc
    private String employeeId;      // milestone owner
    private String username;        // milestone owner username
    private List<TaskResponseDto> tasks; //  tasks belonging to this milestone
}
