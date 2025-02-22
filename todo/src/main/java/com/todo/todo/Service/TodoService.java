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
        //NullPointerException은 예상치못한 예외에서 사용하는 것
        User user = userRepository.findUserByEmailAndProvider(todoCreateDTO.getEmail(), todoCreateDTO.getProvider())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + todoCreateDTO.getEmail()));

        Todo todo = new Todo();
        todo.setTitle(todoCreateDTO.getTask());
        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public void updateTodo(TodoDTO todoDTO) {
        Todo todo = todoRepository.findById(todoDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Todo item not found with id: " + todoDTO.getId()));

        todo.setTitle(todoDTO.getContent());
        todoRepository.save(todo);  // 예외 발생 시 컨트롤러에서 처리
    }


    public void deleteTodo(long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Todo item not found with id: " + id));

        todoRepository.delete(todo);  // Todo 삭제
    }


    public List<Todo> findAll(){
        return todoRepository.findAll();
    }
}
