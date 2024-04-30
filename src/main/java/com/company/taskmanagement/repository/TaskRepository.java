package com.company.taskmanagement.repository;

import com.company.taskmanagement.enums.StateType;
import com.company.taskmanagement.enums.StatusType;
import com.company.taskmanagement.model.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("SELECT t FROM Task t WHERE t.status IN (:statuses)")
    List<Task> findByStatus(List<StatusType> statuses, Pageable pageable);

    List<Task> findByState(StateType state);

    List<Task> findByStartTimeBeforeAndState(String startTime, StateType state);
}
