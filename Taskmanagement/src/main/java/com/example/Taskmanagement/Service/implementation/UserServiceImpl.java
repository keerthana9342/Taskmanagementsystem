package com.example.taskmanagement.Service.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Util.UserMapper;
import com.example.taskmanagement.Model.User;
import com.example.taskmanagement.Repository.UserRepository;
import com.example.taskmanagement.Service.UserService;
import com.example.taskmanagement.dto.Request.UserRequestDto;
import com.example.taskmanagement.dto.Response.UserResponseDto;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {

        User user = UserMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);

        return UserMapper.toDtO(saved);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDtO)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toDtO(user);
    }

    @Override
    public String deleteUser(String id) {

        userRepository.deleteById(id);

        return "User deleted successfully";
    }
}