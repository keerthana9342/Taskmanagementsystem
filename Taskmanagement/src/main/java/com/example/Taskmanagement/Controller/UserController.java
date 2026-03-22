package com.example.taskmanagement.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.taskmanagement.Service.UserService;
import com.example.taskmanagement.dto.Request.UserRequestDto;
import com.example.taskmanagement.dto.Response.UserResponseDto;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return userService.createUser(dto);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }
}