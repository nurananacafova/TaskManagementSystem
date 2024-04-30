package com.company.taskmanagement.dto.response;

import com.company.taskmanagement.model.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserPaginationResponseModel {
    private final List<User> users;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean isLastPage;
}
