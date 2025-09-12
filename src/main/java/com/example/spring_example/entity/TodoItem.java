package com.example.spring_example.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Data
public class TodoItem {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "task must not be empty")
    private String task;

    @NotNull(message="completion must be true or false")
    private Boolean completed;

    // public Long getId() {return id;}
    // public void setId(Long id) {this.id = id;}

    // public String getTask() { return task;}
    // public void setTask(String task) { this.task = task;}

    // public Boolean getCompleted() { return completed;}
    // public void setCompleted(Boolean completed) {this.completed = completed;}

}