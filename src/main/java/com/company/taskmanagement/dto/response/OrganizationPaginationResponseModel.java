package com.company.taskmanagement.dto.response;

import com.company.taskmanagement.model.Organization;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OrganizationPaginationResponseModel {

    private final List<Organization> organizations;
    private final int pageNumber;
    private final int pageSize;
    private final long totalElements;
    private final int totalPages;
    private final boolean isLastPage;

}