package com.company.taskmanagement.service;

import com.company.taskmanagement.dto.request.OrganizationRequestDto;
import com.company.taskmanagement.dto.response.OrganizationResponseDto;

import java.util.List;

public interface OrganizationService {
    List<OrganizationResponseDto> getAllOrganizations(int pageNo, int recordCount, String sortBy);

    OrganizationResponseDto getOrganizationById(Integer id);

    OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequest);

    OrganizationResponseDto updateOrganization(Integer id, OrganizationRequestDto organizationDto);

    void deleteOrganization(Integer id);
}
