package com.company.taskmanagement.controller;

import com.company.taskmanagement.dto.request.TaskRequestDto;
import com.company.taskmanagement.dto.response.TaskPaginationResponseModel;
import com.company.taskmanagement.dto.response.TaskResponseDto;
import com.company.taskmanagement.model.Task;
import com.company.taskmanagement.service.impl.TaskServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {
    private TaskServiceImpl taskServiceImpl;

    public TaskController(TaskServiceImpl taskServiceImpl) {
        this.taskServiceImpl = taskServiceImpl;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public List<TaskResponseDto> getAllTasks(@RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                             @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                             @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        return taskServiceImpl.getAllTasks(pageNumber, size, sort);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Integer id) {
        return new ResponseEntity<>(taskServiceImpl.getTaskById(id), HttpStatus.OK);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto taskRequest) {
        return new ResponseEntity<>(taskServiceImpl.createTask(taskRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Integer id, @RequestBody TaskRequestDto taskDto) {
        TaskResponseDto dto = taskServiceImpl.updateTask(id, taskDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteTask(@PathVariable Integer id) {
        taskServiceImpl.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{taskId}/user/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Task taskUser(@PathVariable Integer taskId, @PathVariable Integer userId) {
        return taskServiceImpl.taskUser(taskId, userId);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<TaskPaginationResponseModel> fetchAllStudents(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "status") String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction
    ) {
        var pageRequestData = PageRequest.of(pageNumber - 1, size, Sort.Direction.valueOf(direction), sort);
        return new ResponseEntity<>(taskServiceImpl.findAllTasks(pageRequestData), HttpStatus.PARTIAL_CONTENT);
    }
}
