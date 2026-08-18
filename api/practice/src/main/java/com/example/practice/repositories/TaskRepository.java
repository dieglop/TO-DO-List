package com.example.practice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.practice.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

    List<Task> findByUser_Id(Long id);

    // @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
    // List<Task> findByUserId(@Param ("id") Long id);


}