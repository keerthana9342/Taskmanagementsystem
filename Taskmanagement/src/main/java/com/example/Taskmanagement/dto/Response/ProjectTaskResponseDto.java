package com.example.taskmanagement.dto.Response;
import lombok.Data;
import java.util.List;
@Data
public class ProjectTaskResponseDto {
    private String projectId;
    private String projectName;
    private List<MileStoneResponseDto> milestones;

}
