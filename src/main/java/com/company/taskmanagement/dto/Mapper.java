package com.company.taskmanagement.dto;

import com.company.taskmanagement.dto.response.OrganizationResponseDto;
import com.company.taskmanagement.dto.response.TaskResponseDto;
import com.company.taskmanagement.dto.response.UserResponseDto;
import com.company.taskmanagement.model.Organization;
import com.company.taskmanagement.model.Task;
import com.company.taskmanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public static OrganizationResponseDto orgToOrganizationResponseDto(Organization organization) {
        OrganizationResponseDto organizationResponseDto = new OrganizationResponseDto();
        organizationResponseDto.setId(organization.getId());
        organizationResponseDto.setOrganizationName(organization.getOrganizationName());
        organizationResponseDto.setStatus(organization.getStatus());
        organizationResponseDto.setCreatedDate(organization.getCreatedDate());
        organizationResponseDto.setUpdateDate(organization.getUpdateDate());
        return organizationResponseDto;
    }

    public static List<OrganizationResponseDto> orgsToOrganizationResponseDtos(List<Organization> organizations) {
        List<OrganizationResponseDto> organizationResponseDtos = new ArrayList<>();
        for (Organization organization : organizations) {
            organizationResponseDtos.add(orgToOrganizationResponseDto(organization));
        }
        return organizationResponseDtos;
    }

    public static TaskResponseDto taskToTaskResponseDto(Task task) {
        TaskResponseDto taskDto = new TaskResponseDto();
        taskDto.setId(task.getId());
        taskDto.setDescription(task.getDescription());
        taskDto.setContent(task.getContent());
//        taskDto.setUserId(task.getUserId());
        taskDto.setState(task.getState());
        taskDto.setStatus(task.getStatus());
        taskDto.setStartTime(task.getStartTime());
        taskDto.setCreatedDate(task.getCreateDate());
        taskDto.setUpdateDate(task.getUpdateDate());
//        taskDto.setUserId(task.getUsers().getClass().getName());
        int id = 0;
        List<User> users = task.getUsers();
        for (User user : users) {
            id = user.getId();
        }
        taskDto.setUserId(id);
        return taskDto;
    }

    public static List<TaskResponseDto> tasksToTaskResponseDtos(List<Task> tasks) {
        List<TaskResponseDto> taskResponseDtos = new ArrayList<>();
        for (Task task : tasks) {
            taskResponseDtos.add(taskToTaskResponseDto(task));
        }
        return taskResponseDtos;
    }

    public static UserResponseDto userToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
//        userResponseDto.setOrgId(user.getOrgId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setStatus(user.getStatus());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setCreateDate(user.getCreateDate());
        userResponseDto.setUpdateDate(user.getUpdateDate());
        int id = 0;
        List<Organization> organizations = user.getOrganizationId();
        for (Organization organization : organizations) {
            id = organization.getId();
        }
        userResponseDto.setOrgId(id);
        return userResponseDto;
    }

    public static List<UserResponseDto> usersToUserResponseDtos(List<User> users) {
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        for (User user : users) {
            userResponseDtos.add(userToUserResponseDto(user));
        }
        return userResponseDtos;
    }


//    private UserRequestDto mapToDto(User user) {
//        UserRequestDto userRequestDto = new UserRequestDto();
//        userRequestDto.setId(user.getId());
//        userRequestDto.setOrgId(user.getOrgId());
//        userRequestDto.setEmail(user.getEmail());
//        userRequestDto.setStatus(user.getStatus());
//        userRequestDto.setCreateDate(user.getCreateDate());
//        userRequestDto.setUpdateDate(user.getUpdateDate());
//        return userRequestDto;
//    }
//
//    private User mapToEntity(UserRequestDto userRequestDto) {
//        User user = new User();
//        user.setOrgId(userRequestDto.getOrgId());
//        user.setEmail(userRequestDto.getEmail());
//        user.setStatus(userRequestDto.getStatus());
//        user.setCreateDate(userRequestDto.getCreateDate());
//        user.setUpdateDate(userRequestDto.getUpdateDate());
//        return user;
//    }
//    private TaskRequestDto mapToDto(Task task) {
//        TaskRequestDto taskDto = new TaskRequestDto();
//        taskDto.setId(task.getId());
//        taskDto.setDescription(task.getDescription());
//        taskDto.setContent(task.getContent());
//        taskDto.setUserId(task.getUserId());
//        taskDto.setState(task.getState());
//        taskDto.setStatus(task.getStatus());
//        taskDto.setStartTime(task.getStartTime());
//        taskDto.setCreatedDate(task.getCreateDate());
//        taskDto.setUpdateDate(task.getUpdateDate());
//        return taskDto;
//    }
//
//    private Task mapToEntity(TaskRequestDto taskDto) {
//        Task task = new Task();
//        task.setDescription(taskDto.getDescription());
//        task.setContent(taskDto.getContent());
//        task.setUserId(taskDto.getUserId());
//        task.setState(taskDto.getState());
//        task.setStatus(taskDto.getStatus());
//        task.setStartTime(taskDto.getStartTime());
//        task.setCreateDate(taskDto.getCreatedDate());
//        task.setUpdateDate(taskDto.getUpdateDate());
//        return task;
//    }

//    public static OrganizationRequestDto mapToDto(Organization organization) {
//        OrganizationRequestDto organizationDto = new OrganizationRequestDto();
//        organizationDto.setId(organization.getId());
//        organizationDto.setOrganizationName(organization.getOrganizationName());
//        organizationDto.setStatus(organization.getStatus());
//        organizationDto.setCreatedDate(organization.getCreatedDate());
//        organizationDto.setUpdateDate(organization.getUpdateDate());
//        return organizationDto;
//    }

//    public static Organization mapToEntity(OrganizationRequestDto organizationDto) {
//        Organization organization = new Organization();
//        organization.setOrganizationName(organizationDto.getOrganizationName());
//        organization.setStatus(organizationDto.getStatus());
//        organization.setCreatedDate(organizationDto.getCreatedDate());
//        organization.setUpdateDate(organizationDto.getUpdateDate());
//        return organization;
//    }
}
