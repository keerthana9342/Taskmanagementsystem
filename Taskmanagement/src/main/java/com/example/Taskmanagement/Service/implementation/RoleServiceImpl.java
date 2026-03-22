package com.example.taskmanagement.Service.implementation;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.Util.RoleMapper;
import com.example.taskmanagement.Model.Role;
import com.example.taskmanagement.Repository.RoleRepository;
import com.example.taskmanagement.Service.RoleService;
import com.example.taskmanagement.dto.Request.RoleRequestDto;
import com.example.taskmanagement.dto.Response.RoleResponseDto;

@Service

public class RoleServiceImpl implements RoleService {
    @Autowired
  private  RoleRepository roleRepository;
  @Override
    
    public List<RoleResponseDto> getAllRoles(int page, int size) {

        Page<Role> rolePage = roleRepository.findAll(PageRequest.of(page, size));

        return rolePage.getContent()
                .stream()
                .map(RoleMapper::toDto)
                .toList();
    }

    // Add Role
    @Override
    public RoleResponseDto addRole(RoleRequestDto dto) {

        Role role = RoleMapper.toEntity(dto);

        Role savedRole = roleRepository.save(role);

        return RoleMapper.toDto(savedRole);
    }

    // Get Role by ID
    @Override
    public RoleResponseDto getRoleById(String id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        return RoleMapper.toDto(role);
    }

    // Delete Role
    @Override
    public String deleteRole(String id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        roleRepository.delete(role);

        return "Role deleted successfully";
    }

    // Update Role
    @Override
    public RoleResponseDto updateRole(String id, RoleRequestDto dto) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());

        Role updatedRole = roleRepository.save(role);

        return RoleMapper.toDto(updatedRole);
    }
}




