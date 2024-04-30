package com.company.taskmanagement.dto.response;

import com.company.taskmanagement.enums.StateType;
import com.company.taskmanagement.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDto {
    private int id;
    private String description;
    private String content;
    private int userId;
    private StateType state;
    private StatusType status;
    private String startTime;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
