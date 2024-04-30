package com.company.taskmanagement.service.impl;

import com.company.taskmanagement.dto.Mapper;
import com.company.taskmanagement.dto.request.OrganizationRequestDto;
import com.company.taskmanagement.dto.response.OrganizationPaginationResponseModel;
import com.company.taskmanagement.dto.response.OrganizationResponseDto;
import com.company.taskmanagement.exception.DataNotFoundException;
import com.company.taskmanagement.model.Organization;
import com.company.taskmanagement.enums.StatusType;
import com.company.taskmanagement.repository.OrganizationRepository;
import com.company.taskmanagement.service.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    public final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public List<OrganizationResponseDto> getAllOrganizations(int pageNo, int recordCount, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, recordCount, Sort.by(sortBy));
        return Mapper.orgsToOrganizationResponseDtos(organizationRepository.findByStatus(Arrays.asList(StatusType.ACTIVE, StatusType.INACTIVE), pageable));
    }

    @Override
    public OrganizationResponseDto getOrganizationById(Integer id) {
        Organization organization = organizationRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find Organization with id: " + id));
        ;
        if (organization.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Organization with id: " + id);
        }
        return Mapper.orgToOrganizationResponseDto(organization);
    }

    @Override
    public OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequest) {
        Organization organization = new Organization();
        organization.setOrganizationName(organizationRequest.getOrganizationName());
        organization.setStatus(organizationRequest.getStatus());
        return Mapper.orgToOrganizationResponseDto(organizationRepository.save(organization));
    }

    @Override
    public OrganizationResponseDto updateOrganization(Integer id, OrganizationRequestDto organizationDto) {
        Organization foundedOrganization = organizationRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find Organization with id: " + id));
        if (foundedOrganization.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Organization with id: " + id);
        }
        foundedOrganization.setOrganizationName(organizationDto.getOrganizationName());
        foundedOrganization.setStatus(organizationDto.getStatus());
        Organization updatedOrganization = organizationRepository.save(foundedOrganization);

        return Mapper.orgToOrganizationResponseDto(updatedOrganization);
    }

    @Override
    public void deleteOrganization(Integer id) {
        Organization foundedOrganization = organizationRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException("Cannot find Organization with id: " + id));
        if (foundedOrganization.getStatus().equals(StatusType.DELETED)) {
            throw new DataNotFoundException("Cannot find Organization with id: " + id);
        }
        foundedOrganization.setStatus(StatusType.DELETED);
        organizationRepository.save(foundedOrganization);
    }

    public OrganizationPaginationResponseModel findAllOrganizations(PageRequest pageable) {
        var organizationPage = this.organizationRepository.findAll(pageable);
        return buildResponse(organizationPage);
    }

    private OrganizationPaginationResponseModel buildResponse(Page organizationPage) {
        return OrganizationPaginationResponseModel.builder()
                .pageNumber(organizationPage.getNumber() + 1)
                .pageSize(organizationPage.getSize())
                .totalElements(organizationPage.getTotalElements())
                .totalPages(organizationPage.getTotalPages())
                .organizations(organizationPage.toList())
                .isLastPage(organizationPage.isLast())
                .build();
    }
}
