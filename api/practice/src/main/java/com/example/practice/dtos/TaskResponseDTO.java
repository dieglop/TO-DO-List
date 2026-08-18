package com.example.practice.dtos;

import com.example.practice.models.Task;

public record TaskResponseDTO(Long id, String description) {
    
    public TaskResponseDTO(Task task){
        this(task.getId(), task.getDescription());
    }

}
