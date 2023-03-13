package com.assignment.repository;

import com.assignment.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByDueDateBetweenOrderByEmployeeAsc(LocalDate startDate, LocalDate endDate);
    Optional<Task> findByTitle(String title);
}