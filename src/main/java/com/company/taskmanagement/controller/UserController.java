package com.company.taskmanagement.controller;

import com.company.taskmanagement.dto.request.UserRequestDto;
import com.company.taskmanagement.dto.response.UserPaginationResponseModel;
import com.company.taskmanagement.dto.response.UserResponseDto;
import com.company.taskmanagement.model.User;
import com.company.taskmanagement.service.impl.UserServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    private UserServiceImpl userServiceImple;

    public UserController(UserServiceImpl userServiceImple) {
        this.userServiceImple = userServiceImple;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public List<UserResponseDto> getAllUsers(@RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                             @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        return userServiceImple.getAllUsers(pageNumber, size, sort);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userServiceImple.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Integer id, @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto dto = userServiceImple.updateUser(id, userRequestDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        userServiceImple.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{userId}/organization/{orgId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User userOrganization(@PathVariable Integer userId, @PathVariable Integer orgId) {
        return userServiceImple.userOrganization(userId, orgId);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<UserPaginationResponseModel> fetchAllStudents(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "status") String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction
    ) {
        var pageRequestData = PageRequest.of(pageNumber - 1, size, Sort.Direction.valueOf(direction), sort);
        return new ResponseEntity<>(userServiceImple.findAllUsers(pageRequestData), HttpStatus.PARTIAL_CONTENT);
    }
}
