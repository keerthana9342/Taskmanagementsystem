package com.example.Util;
import com.example.taskmanagement.Model.User;
import com.example.taskmanagement.dto.Request.UserRequestDto;
import com.example.taskmanagement.dto.Response.UserResponseDto;
public class UserMapper {
    public static User toEntity(UserRequestDto dto){
        User user=new User();
        user.setUsername(dto.getUsername());
        user.setUserId(dto.getUserId());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        user.setStatus(dto.getStatus());
        return user;
    }
    public static UserResponseDto toDtO(User user){
        UserResponseDto dto=new UserResponseDto();
        dto.setUserId(user.getUserId());
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        return dto;
    }

}
