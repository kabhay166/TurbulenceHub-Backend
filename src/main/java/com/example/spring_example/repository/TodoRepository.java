package com.example.spring_example.repository;

import com.example.spring_example.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<TodoItem, Long> {
    
}
