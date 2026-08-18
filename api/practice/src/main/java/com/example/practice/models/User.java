package com.example.practice.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.practice.dtos.UserDTO;
import com.example.practice.enums.ProfileEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = User.TABLE_NAME)
public class User {

    public static final String TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    public Long id;

    @Column(name = "username", length = 100, nullable = false, unique  = true)
    @NotBlank
    @Size(min = 2, max = 100)
    private String username;

    @Column(name="password", length = 60, nullable=false)
    @NotBlank
    @Size(min = 8, max = 60)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<Task>();
    
    public User(){}

    public User(Long id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User(UserDTO userDTO){
        this.username = userDTO.username();
        this.password = userDTO.password();
    }

    @ElementCollection(fetch=FetchType.EAGER)
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    @CollectionTable(name="user_profile")
    @Column(name = "profile", nullable = false)
    private final Set<Integer> profiles = new HashSet<>();

    public Set<ProfileEnum> getProfiles() {
        return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profile){
        this.profiles.add(profile.getCode());
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    

}
