package com.company.taskmanagement.service.impl;

import com.company.taskmanagement.dto.Mapper;
import com.company.taskmanagement.dto.request.UserRequestDto;
import com.company.taskmanagement.dto.response.UserPaginationResponseModel;
import com.company.taskmanagement.dto.response.UserResponseDto;
import com.company.taskmanagement.exception.DataNotFoundException;
import com.company.taskmanagement.exception.UserAlreadyExistsException;
import com.company.taskmanagement.model.Organization;
import com.company.taskmanagement.enums.StatusType;
import com.company.taskmanagement.model.User;
import com.company.taskmanagement.repository.OrganizationRepository;
import com.company.taskmanagement.repository.UserRepository;
import com.company.taskmanagement.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private OrganizationRepository organizationRepository;

    public UserServiceImpl(UserRepository userRepository, OrganizationRepository organizationRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public List<UserResponseDto> getAllUsers(int pageNo, int recordCount, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, recordCount, Sort.by(sortBy));
        return Mapper.usersToUserResponseDtos(userRepository.findByStatus(Arrays.asList(StatusType.ACTIVE, StatusType.INACTIVE), pageable));
    }

    @Override
    public UserResponseDto getUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find User with id: " + id));
        if (user.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find User with id: " + id);
        }
        return Mapper.userToUserResponseDto(user);
    }

//    @Override
//    public UserResponseDto createUser(User user) {
//        List<Organization> organizations = new ArrayList<>();
//        Organization organization = organizationRepository.findById(user.getOrgId()).orElseThrow(() ->
//                new DataNotFoundException("Cannot find Organization"));
//        if (organization.getStatus().equals(StatusType.DELETED)) {
//            throw new DataNotFoundException("Cannot find User with id: " + user.getOrganizationId());
//        }
//        organizations.add(organization);
//        user.setOrganizationId(organizations);
//        return Mapper.userToUserResponseDto(userRepository.save(user));
//    }

    @Override
    public UserResponseDto updateUser(Integer id, UserRequestDto userRequestDto) {
        User foundedUser = userRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find User with id: " + id));
        Organization organization = organizationRepository.findById(userRequestDto.getOrgId()).orElseThrow(() ->
                new DataNotFoundException("Cannot find Organization with id: " + userRequestDto.getOrgId()));
        if (foundedUser.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find User with id: " + id);
        } else if (organization.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Organization with id: " + organization.getId());
        }else if (emailExists(userRequestDto.getEmail()))
            throw new UserAlreadyExistsException("User already exists with Email: " + userRequestDto.getEmail());

        foundedUser.setOrgId(userRequestDto.getOrgId());
        foundedUser.setEmail(userRequestDto.getEmail());
        foundedUser.setStatus(userRequestDto.getStatus());
        User updatedUser = userRepository.save(foundedUser);

        return Mapper.userToUserResponseDto(updatedUser);
    }

    public boolean emailExists(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }
    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find User with id: " + id));
        if (user.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find User with id: " + id);
        }
        user.setStatus(StatusType.DELETED);
        userRepository.save(user);
    }

    @Override
    public User userOrganization(Integer userId, Integer orgId) {
        List<Organization> organizations = null;
        User user = userRepository.findById(userId).orElseThrow(() ->
                new DataNotFoundException("Cannot find User with id: " + userId));
        Organization organization = organizationRepository.findById(orgId).orElseThrow(() ->
                new DataNotFoundException("Cannot find Organization with id: " + orgId));
        if (user.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find User with id: " + userId);
        } else if (organization.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Organization with id: " + orgId);
        }
        organizations = user.getOrganizationId();
        organizations.add(organization);
        user.setOrganizationId(organizations);
        return userRepository.save(user);
    }

    public UserPaginationResponseModel findAllUsers(PageRequest pageable) {
        var userPage = this.organizationRepository.findAll(pageable);
        return buildResponse(userPage);
    }

    private UserPaginationResponseModel buildResponse(Page userPage) {

        return UserPaginationResponseModel.builder()
                .pageNumber(userPage.getNumber() + 1)
                .pageSize(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .users(userPage.toList())
                .isLastPage(userPage.isLast())
                .build();
    }
}
