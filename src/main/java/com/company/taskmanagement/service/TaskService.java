package com.company.taskmanagement.service;

import com.company.taskmanagement.dto.request.TaskRequestDto;
import com.company.taskmanagement.dto.response.TaskResponseDto;
import com.company.taskmanagement.model.Task;

import java.util.List;


public interface TaskService {
    List<TaskResponseDto> getAllTasks(int pageNo, int recordCount, String sortBy);

    TaskResponseDto getTaskById(Integer id);

    TaskResponseDto createTask(TaskRequestDto taskRequest);

    TaskResponseDto updateTask(Integer id, TaskRequestDto taskDto);

    void deleteTask(Integer id);

    Task taskUser(Integer taskId, Integer userId);
}
