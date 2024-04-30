package com.company.taskmanagement.kafka.consumer;

import com.company.taskmanagement.model.Task;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class MessageConsumer {

    @KafkaListener(topics = "task-topic", groupId = "task-group-id")
    public void listen(Task message) {
        System.out.println("Received task: " + message);
    }
}
