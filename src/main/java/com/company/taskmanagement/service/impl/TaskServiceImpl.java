package com.company.taskmanagement.service.impl;

import com.company.taskmanagement.dto.Mapper;
import com.company.taskmanagement.dto.request.TaskRequestDto;
import com.company.taskmanagement.dto.response.TaskPaginationResponseModel;
import com.company.taskmanagement.dto.response.TaskResponseDto;
import com.company.taskmanagement.enums.StateType;
import com.company.taskmanagement.enums.StatusType;
import com.company.taskmanagement.exception.DataNotFoundException;
import com.company.taskmanagement.kafka.producer.MessageProducer;
import com.company.taskmanagement.model.*;
import com.company.taskmanagement.repository.TaskRepository;
import com.company.taskmanagement.repository.UserRepository;
import com.company.taskmanagement.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final MessageProducer messageProducer;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, MessageProducer messageProducer) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.messageProducer = messageProducer;
    }

    @Override
    public List<TaskResponseDto> getAllTasks(int pageNo, int recordCount, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, recordCount, Sort.by(sortBy));
        return Mapper.tasksToTaskResponseDtos(taskRepository.findByStatus(Arrays.asList(StatusType.ACTIVE, StatusType.INACTIVE), pageable));
    }

    @Override
    public TaskResponseDto getTaskById(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find Task with id: " + id));
        if (task.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Task with id: " + id);
        }
        return Mapper.taskToTaskResponseDto(task);
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto taskRequest) {
        Task task = new Task();
        // set many to many relationship:
        List<User> users = new ArrayList<>();
        User user = userRepository.findById(taskRequest.getUserId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find User"));
        if (user.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find User with id: " + user.getId());
        }
        users.add(user);
        task.setUsers(users);

        task.setDescription(taskRequest.getDescription());
        task.setContent(taskRequest.getContent());
        task.setUserId(taskRequest.getUserId());
        task.setState(taskRequest.getState());
        task.setStatus(taskRequest.getStatus());


        // set startDate 2 minute after creation date
        LocalDateTime newTime = LocalDateTime.now().plusMinutes(2);
        String result = newTime.toString();
        task.setStartTime(result);

        return Mapper.taskToTaskResponseDto(taskRepository.save(task));
    }

    @Override
    public TaskResponseDto updateTask(Integer id, TaskRequestDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find Task with id: " + id));

        User user = userRepository.findById(taskDto.getUserId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find User with id: " + taskDto.getUserId()));
        if (task.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Task with id: " + id);
        } else if (user.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find User with id: " + user.getId());
        }

        task.setDescription(taskDto.getDescription());
        task.setContent(taskDto.getContent());
        task.setUserId(taskDto.getUserId());
        task.setState(taskDto.getState());
        task.setStatus(taskDto.getStatus());
        task.setStartTime(taskDto.getStartTime());
        Task updatedTask = taskRepository.save(task);

        return Mapper.taskToTaskResponseDto(updatedTask);
    }

    @Override
    public void deleteTask(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find Task with id: " + id));
        if (task.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Task with id: " + id);
        }
        task.setStatus(StatusType.DELETED);
        taskRepository.save(task);
    }

    @Override
    public Task taskUser(Integer taskId, Integer userId) {
        List<User> users = null;
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new DataNotFoundException("Cannot find Task with id: " + taskId));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new DataNotFoundException("Cannot find User with id: " + userId));
        if (user.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find User with id: " + user.getId());
        } else if (task.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Task with id: " + task.getId());
        }

        users = task.getUsers();
        users.add(user);
        task.setUsers(users);
        return taskRepository.save(task);
    }

    public TaskPaginationResponseModel findAllTasks(PageRequest pageable) {
        var taskPage = this.taskRepository.findAll(pageable);
        return buildResponse(taskPage);
    }

    private TaskPaginationResponseModel buildResponse(Page organizationPage) {

        return TaskPaginationResponseModel.builder()
                .pageNumber(organizationPage.getNumber() + 1)
                .pageSize(organizationPage.getSize())
                .totalElements(organizationPage.getTotalElements())
                .totalPages(organizationPage.getTotalPages())
                .tasks(organizationPage.toList())
                .isLastPage(organizationPage.isLast())
                .build();
    }

    // change task state from PENDING to RUNNING in start time
    @Transactional
    public void updateTaskState() {
        LocalDateTime currentTime = LocalDateTime.now();
        String time = currentTime.toString();
        List<Task> pendingTasks = taskRepository.findByStartTimeBeforeAndState(time, StateType.PENDING);
        for (Task task : pendingTasks) {
            task.setState(StateType.RUNNING);
            taskRepository.save(task);
        }
    }

    // send message to kafka and change state from RUNNING to FINISHED
    public void sendMessageToKafka() {
        List<Task> runningTasks = taskRepository.findByState(StateType.RUNNING);
        for (Task task : runningTasks) {
            messageProducer.sendMessage("task-topic", task);
            task.setState(StateType.FINISHED);
            taskRepository.save(task);
        }
    }
}
