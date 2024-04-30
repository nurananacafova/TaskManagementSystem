package com.company.taskmanagement.service;

import com.company.taskmanagement.dto.request.UserRequestDto;
import com.company.taskmanagement.dto.response.UserResponseDto;
import com.company.taskmanagement.model.User;

import java.util.List;

public interface UserService {

    List<UserResponseDto> getAllUsers(int pageNo, int recordCount, String sortBy);

    UserResponseDto getUserById(Integer id);

//    UserResponseDto createUser(User user);

    UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto);

    void deleteUser(Integer id);

    User userOrganization(Integer userId, Integer orgId);
}
