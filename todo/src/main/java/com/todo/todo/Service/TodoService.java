package com.todo.todo.Service;

import com.todo.todo.Entity.Todo;
import com.todo.todo.Repository.TodoRepository;

import java.util.List;

public class TodoService {

    private final TodoRepository todoRepository;


    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo createTodo(Todo todo){
        return todoRepository.save(todo);
    }

    public List<Todo> findAll(){
        return todoRepository.findAll();
    }
}
