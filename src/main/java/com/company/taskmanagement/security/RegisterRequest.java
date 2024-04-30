package com.company.taskmanagement.security;


import com.company.taskmanagement.enums.Role;
import com.company.taskmanagement.enums.StatusType;
import com.company.taskmanagement.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private int orgId;
//    private List<Organization> organizationId;
    private String password;
    private String email;
    private StatusType status = StatusType.ACTIVE;
    private Role role;
}
