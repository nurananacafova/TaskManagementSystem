package com.company.taskmanagement.dto.response;

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
public class OrganizationResponseDto {
    private int id;
    private String organizationName;
    private StatusType status;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;

}
