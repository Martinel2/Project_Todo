package com.todo.todo.Service;

import com.todo.todo.Dto.TodoCreateDTO;
import com.todo.todo.Dto.TodoDTO;
import com.todo.todo.Entity.Todo;
import com.todo.todo.Entity.User;
import com.todo.todo.Repository.TodoRepository;
import com.todo.todo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;

    public List<TodoDTO> getTodosByUser(String email, String provider) {
        List<Todo> todos = todoRepository.findByUserEmailAndUserProvider(email, provider);
        return todos.stream().map(TodoDTO::new).collect(Collectors.toList());
    }


    public Todo createTodo(TodoCreateDTO todoCreateDTO) {
        User user = userRepository.findUserByEmailAndProvider(todoCreateDTO.getEmail(), todoCreateDTO.getProvider()).orElse(null);
        if (user != null) {
            Todo todo = new Todo();
            todo.setTitle(todoCreateDTO.getTask());
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
