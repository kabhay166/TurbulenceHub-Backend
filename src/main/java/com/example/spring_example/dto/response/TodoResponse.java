package com.example.spring_example.dto.response;


public class TodoResponse {
    
    private Long id;
    private String task;
    private Boolean completed;

    public TodoResponse(Long id, String task, Boolean completed) {
        this.id = id;
        this.task = task;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public Boolean getcompleted() {
        return completed;
    }
}
