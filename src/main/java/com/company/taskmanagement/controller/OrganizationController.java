package com.company.taskmanagement.controller;

import com.company.taskmanagement.dto.request.OrganizationRequestDto;
import com.company.taskmanagement.dto.response.OrganizationPaginationResponseModel;
import com.company.taskmanagement.dto.response.OrganizationResponseDto;
import com.company.taskmanagement.service.impl.OrganizationServiceImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/organization")
public class OrganizationController {
    public OrganizationServiceImpl organizationServiceImpl;

    public OrganizationController(OrganizationServiceImpl organizationServiceImpl) {
        this.organizationServiceImpl = organizationServiceImpl;
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<OrganizationResponseDto>> getAllOrganizations(@RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                                                             @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                                             @RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {
        List<OrganizationResponseDto> list = organizationServiceImpl.getAllOrganizations(pageNumber, size, sort);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<OrganizationResponseDto> getOrganizationById(@PathVariable int id) {
        return new ResponseEntity<>(organizationServiceImpl.getOrganizationById(id), HttpStatus.OK);
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<OrganizationResponseDto> createOrganization(@RequestBody OrganizationRequestDto organizationRequest) {
        return new ResponseEntity<>(organizationServiceImpl.createOrganization(organizationRequest), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrganizationResponseDto> updateOrganization(@PathVariable Integer id, @RequestBody OrganizationRequestDto organizationDto) {
        OrganizationResponseDto dto = organizationServiceImpl.updateOrganization(id, organizationDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteOrganization(@PathVariable Integer id) {
        organizationServiceImpl.deleteOrganization(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<OrganizationPaginationResponseModel> fetchAllStudents(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "status") String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "ASC") String direction
    ) {
        var pageRequestData = PageRequest.of(pageNumber - 1, size, Sort.Direction.valueOf(direction), sort);
        return new ResponseEntity<>(organizationServiceImpl.findAllOrganizations(pageRequestData), HttpStatus.PARTIAL_CONTENT);
    }
}
