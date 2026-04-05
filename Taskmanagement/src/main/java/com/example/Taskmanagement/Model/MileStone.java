package com.example.taskmanagement.Model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
@Data
public class MileStone {

    private String milestoneId;
    private String milestoneName;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String status;
    private String employeeId;
    private String username;
    

}
