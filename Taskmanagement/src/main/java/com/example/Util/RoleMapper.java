package com.example.Util;
import com.example.taskmanagement.Model.Role;
import com.example.taskmanagement.dto.Request.RoleRequestDto;  
import com.example.taskmanagement.dto.Response.RoleResponseDto;
public class RoleMapper {
    public static Role toEntity(RoleRequestDto roleRequestDto) {
        Role role = new Role();
        role.setRoleName(roleRequestDto.getRoleName());
        role.setDescription(roleRequestDto.getDescription());
        return role;
    }
    public static RoleResponseDto toDto(Role role) {
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setId(role.getId());
        roleResponseDto.setRoleName(role.getRoleName());
        roleResponseDto.setDescription(role.getDescription());
        return roleResponseDto;
    }

}
