package com.example.Util;

import java.util.ArrayList;
import java.util.List;

import com.example.taskmanagement.Model.MileStone;
import com.example.taskmanagement.dto.Response.MileStoneResponseDto;

public class MileStoneMapper {

    // Milestone Mapper
    public static MileStoneResponseDto toDto(MileStone milestone) {
        MileStoneResponseDto dto = new MileStoneResponseDto();
        dto.setMilestoneId(milestone.getMilestoneId());
        dto.setMilestoneName(milestone.getMilestoneName());
        dto.setStartDate(milestone.getStartDate());
        dto.setEndDate(milestone.getEndDate());
        dto.setStatus(milestone.getStatus());
        dto.setEmployeeId(milestone.getEmployeeId());
        dto.setUsername(milestone.getUsername());
        
        return dto;
    }

    // List Mapper
    public static List<MileStoneResponseDto> toDtoList(List<MileStone> milestones) {
        if (milestones == null) {
            return new ArrayList<>();
        }
        return milestones.stream()
                .map(MileStoneMapper::toDto)
                .toList();
    }
}