

package com.example.Util;

import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.dto.Request.ProjectRequestDto;
import com.example.taskmanagement.dto.Response.ProjectResposeDto;

public class ProjectMapper {

    public static Project toEntity(ProjectRequestDto dto) {
        Project project = new Project();
        project.setProjectId(dto.getProjectId());
        project.setProjectName(dto.getProjectName());
        project.setActive(dto.isActive());
        project.setEmployeeIds(dto.getEmployeeIds());
        return project;
    }

    public static ProjectResposeDto toDto(Project project) {
        ProjectResposeDto dto = new ProjectResposeDto();
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setActive(project.isActive());
        dto.setEmployeeIds(project.getEmployeeIds());
        return dto;
    }
}
