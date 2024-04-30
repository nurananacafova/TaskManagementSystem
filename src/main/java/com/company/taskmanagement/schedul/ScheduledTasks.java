package com.company.taskmanagement.schedul;

import com.company.taskmanagement.repository.UserRepository;
import com.company.taskmanagement.service.impl.TaskServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("Schedule")
public class ScheduledTasks {
    private final TaskServiceImpl taskService;

    private final UserRepository userRepository;

    public ScheduledTasks(UserRepository userRepository, TaskServiceImpl taskService) {
        this.userRepository = userRepository;
        this.taskService = taskService;
    }

    // run every 10 second
    @Scheduled(fixedDelay = 10000)
    public void updateTaskStatus() {
        taskService.updateTaskState();
    }

    // run every 20 second
    @Scheduled(fixedDelay = 20000)
    public void sendMessageToKafka() {
        taskService.sendMessageToKafka();
    }

}
