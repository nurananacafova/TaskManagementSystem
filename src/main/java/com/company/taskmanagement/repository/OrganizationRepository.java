package com.company.taskmanagement.repository;

import com.company.taskmanagement.enums.StatusType;
import com.company.taskmanagement.model.Organization;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer>, PagingAndSortingRepository<Organization, Integer> {
    @Query("SELECT o FROM Organization o WHERE o.status IN (:statuses)")
    List<Organization> findByStatus(List<StatusType> statuses, Pageable pageable);

}
