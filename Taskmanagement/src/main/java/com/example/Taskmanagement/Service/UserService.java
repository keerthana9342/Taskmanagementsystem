package com.example.taskmanagement.Service;

import java.util.List;

import com.example.taskmanagement.dto.Request.UserRequestDto;
import com.example.taskmanagement.dto.Response.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(UserRequestDto dto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(String id);

    String deleteUser(String id);
}