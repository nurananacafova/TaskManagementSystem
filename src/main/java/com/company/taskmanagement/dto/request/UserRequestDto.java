package com.company.taskmanagement.dto.request;

import com.company.taskmanagement.enums.Role;
import com.company.taskmanagement.enums.StatusType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @JsonIgnore
    private int id;
    private int orgId;
    private String email;
    private StatusType status;
    private Role role;
    @JsonIgnore
    private LocalDateTime createDate;
    @JsonIgnore
    private LocalDateTime updateDate;
}
