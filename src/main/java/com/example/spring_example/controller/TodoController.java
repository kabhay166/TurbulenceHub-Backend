package com.example.spring_example.controller;
import com.example.spring_example.dto.request.TodoRequest;
import com.example.spring_example.dto.response.TodoResponse;
import com.example.spring_example.service.TodoService;
import com.example.spring_example.repository.TodoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @GetMapping("")
    public List<TodoResponse> getAll() {
        return todoService.getAll();
    }

    @GetMapping("/{id}")
    public TodoResponse getById(@PathVariable Long id) {
        return todoService.getById(id);
    }
    

    @PostMapping("/create")
    public TodoResponse create(@Valid @RequestBody TodoRequest todoRequest) {
        return todoService.create(todoRequest);
    }

    @DeleteMapping("/delete/{id}")
    public TodoResponse deleteTodo(@NotNull @PathVariable Long id) {
        return todoService.delete(id);
    }

    @PutMapping("/update/{id}")
    public TodoResponse update(@PathVariable Long id, @RequestBody TodoRequest todoRequest) {
        
       return todoService.update(id,todoRequest);
    }
    
}

