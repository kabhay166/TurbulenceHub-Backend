package com.example.spring_example.service;

import com.example.spring_example.dto.request.TodoRequest;
import com.example.spring_example.dto.response.TodoResponse;
import com.example.spring_example.entity.TodoItem;
import com.example.spring_example.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {


    TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoResponse> getAll() {
        return todoRepository.findAll().stream()
                .map(todo -> new TodoResponse(todo.getId(),todo.getTask(),todo.getCompleted()))
                .collect(Collectors.toList());
    }

    public TodoResponse getById(Long id) {
        TodoItem todoItem = todoRepository.getReferenceById(id);
        return new TodoResponse(id, todoItem.getTask(), todoItem.getCompleted());
    }

    public TodoResponse create(TodoRequest todoRequest) {
        TodoItem item = new TodoItem();
        item.setTask(todoRequest.getTask());
        item.setCompleted(todoRequest.getCompleted());
        TodoItem saved = todoRepository.save(item);
        return new TodoResponse(saved.getId(), saved.getTask(), saved.getCompleted());
    }

    public TodoResponse delete(Long id) {
        TodoItem deletedTodoItem = todoRepository.getReferenceById(id);
        TodoResponse deletedTodo = new TodoResponse(id, deletedTodoItem.getTask(), deletedTodoItem.getCompleted());
        todoRepository.deleteById(id);
        return deletedTodo;
    }

    public TodoResponse update(Long id, TodoRequest todoRequest) {

        TodoItem todoItem = todoRepository.getReferenceById(id);
        todoItem.setTask(todoRequest.getTask());
        todoItem.setCompleted(todoRequest.getCompleted());
        todoRepository.save(todoItem);
        return new TodoResponse(todoItem.getId(),todoItem.getTask(),todoItem.getCompleted());
    }

}
