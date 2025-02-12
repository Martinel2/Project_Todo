package com.todo.todo.Service;

import com.todo.todo.Entity.Todo;
import com.todo.todo.Entity.User;
import com.todo.todo.Repository.TodoRepository;
import com.todo.todo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Todo> getTodosByUser(String email, String provider) {
        return todoRepository.findByUserEmailAndUserProvider(email, provider);
    }

    public Todo createTodo(String email, String provider, String task) {
        User user = userRepository.findUserByEmailAndProvider(email, provider).orElse(null);
        if (user != null) {
            Todo todo = new Todo();
            todo.setTitle(task);
            todo.setUser(user);
            return todoRepository.save(todo);
        }
        return null; // or throw exception
    }


    public boolean removeTodo(Todo todo){
        try{
            todoRepository.delete(todo);
        }catch (IllegalArgumentException e){
            return false;
        }
        return true;
    }

    public List<Todo> findAll(){
        return todoRepository.findAll();
    }
}
