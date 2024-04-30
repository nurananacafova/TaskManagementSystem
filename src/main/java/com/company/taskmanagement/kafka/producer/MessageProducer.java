package com.company.taskmanagement.kafka.producer;


import com.company.taskmanagement.model.Task;
import com.company.taskmanagement.repository.TaskRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class MessageProducer {
    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, Task> kafkaTemplate;

    public MessageProducer(KafkaTemplate<String, Task> kafkaTemplate, TaskRepository taskRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.taskRepository = taskRepository;
    }

    public void sendMessage(String topic, Task message) {
        kafkaTemplate.send(topic, message);
    }
}
