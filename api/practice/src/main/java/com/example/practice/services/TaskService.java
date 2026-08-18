package com.example.practice.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.practice.dtos.TaskResponseDTO;
import com.example.practice.enums.ProfileEnum;
import com.example.practice.models.Task;
import com.example.practice.models.User;
import com.example.practice.repositories.TaskRepository;
import com.example.practice.security.UserSpringSecurity;
import com.example.practice.services.exceptions.AuthorizationException;
import com.example.practice.services.exceptions.DataBindingViolationException;
import com.example.practice.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService){
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task findById(Long id){

        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
            "Task não encontrada! " + id + ", Tipo: " + Task.class.getName()
        ));

        UserSpringSecurity userSpringSecurity = UserService.authenticate();

        if(!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task)){
            throw new AuthorizationException("Acesso Negado!");
        }
        return task;
    }

    public List<TaskResponseDTO> findAllByUser(){
        
        UserSpringSecurity userSpringSecurity = UserService.authenticate();

        if(!Objects.nonNull(userSpringSecurity)){
            throw new AuthorizationException("Usuário inexistente!");
        }
        List<Task> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());

        return tasks.stream().map(t -> new TaskResponseDTO(t)).toList();
    }

    @Transactional
    public Task create (Task task){

        UserSpringSecurity userSpringSecurity = UserService.authenticate();

        if(!Objects.nonNull(userSpringSecurity)){
            throw new AuthorizationException("Acesso Negado!");
        }

        User user = this.userService.findById(userSpringSecurity.getId());
        task.setId(null);
        task.setUser(user);
        task = this.taskRepository.save(task);

        return task;
    }

    @Transactional
    public Task update (Task task){
        UserSpringSecurity userSpringSecurity = UserService.authenticate();

        User user = userService.findById(userSpringSecurity.getId());
        Task newTask = findById(task.getId());
        newTask.setDescription(task.getDescription());
        newTask.setUser(user);

        return this.taskRepository.save(newTask);
    }

    public TaskResponseDTO delete(Long id){

        try {
            Task task = this.findById(id);
            TaskResponseDTO taskResponseDTO = new TaskResponseDTO(task);
            this.taskRepository.deleteById(id);
            
            return taskResponseDTO;

        } catch (Exception e) {

           throw new DataBindingViolationException("Não possível apagar pois existem entidades relacionadas!");

        }
        
    }


    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
