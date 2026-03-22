package com.example.taskmanagement.Service;
import java.util.List;
import com.example.taskmanagement.dto.Request.RoleRequestDto;
import com.example.taskmanagement.dto.Response.RoleResponseDto;
public interface RoleService {
    List<RoleResponseDto> getAllRoles(int page, int size);
    RoleResponseDto addRole(RoleRequestDto dto);
    RoleResponseDto getRoleById(String id);
    String deleteRole(String id);
    RoleResponseDto updateRole(String id, RoleRequestDto dto);

    

}
