
    
package com.example.taskmanagement.dto.Request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MileStoneRequestDto {

    @NotBlank(message = "Milestone name is required")
    private String milestoneName;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    private String status;

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Username is required")
    private String username;
}

