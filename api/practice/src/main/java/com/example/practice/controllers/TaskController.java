package com.example.practice.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.practice.dtos.TaskResponseDTO;
import com.example.practice.models.Task;
import com.example.practice.services.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {
    
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id){
        
        Task task = this.taskService.findById(id);

        return ResponseEntity.ok().body(task);
    }

    @GetMapping("/user")
    public ResponseEntity<List<TaskResponseDTO>> findAllByUserId(){

        List<TaskResponseDTO> tasks = this.taskService.findAllByUser();

        return ResponseEntity.ok().body(tasks);
    }
    
    @PostMapping
    @Validated
    public ResponseEntity<Task> create(@Valid @RequestBody Task task){

        this.taskService.create(task);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}").buildAndExpand(task.getId()).toUri();

        return ResponseEntity.created(uri).body(task);
    }

    @PutMapping("/{id}")
    @Validated 
    public ResponseEntity<Task> update(@Valid @RequestBody Task task, @PathVariable Long id){

        task.setId(id);
        Task newTask = this.taskService.update(task);

        return ResponseEntity.ok().body(newTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> delete(@PathVariable Long id){

       TaskResponseDTO taskResponseDTO = this.taskService.delete(id);

        return ResponseEntity.ok(taskResponseDTO);

    }


}
