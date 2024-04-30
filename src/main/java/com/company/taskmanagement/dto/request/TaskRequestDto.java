package com.company.taskmanagement.dto.request;

import com.company.taskmanagement.enums.StateType;
import com.company.taskmanagement.enums.StatusType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {
    @JsonIgnore
    private int id;
    private String description;
    private String content;
    private int userId;
    private StateType state;
    private StatusType status;
    @JsonIgnore
    private String startTime;
    @JsonIgnore
    private LocalDateTime createdDate;
    @JsonIgnore
    private LocalDateTime updateDate;
}
