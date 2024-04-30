package com.company.taskmanagement.repository;

import com.company.taskmanagement.enums.StatusType;
import com.company.taskmanagement.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.status IN (:status)")
    List<User> findByStatus(List<StatusType> status, Pageable pageable);

    Optional<User> findByEmail(String email);
}
