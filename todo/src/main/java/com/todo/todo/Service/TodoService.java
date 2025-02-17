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

    public boolean updateTodo(TodoDTO todoDTO) {
        Optional<Todo> todo = todoRepository.findById(todoDTO.getId());  // id로 Todo 조회
        if (todo.isPresent()) {
            Todo upTodo = todo.get();
            upTodo.setTitle(todoDTO.getContent());
            // save() 메소드가 반환하는 객체와 비교하지 말고, 예외가 발생하지 않도록 처리
            try {
                todoRepository.save(upTodo);  // 데이터베이스에 업데이트
                return true;  // 저장이 성공하면 true 반환
            } catch (Exception e) {
                return false;  // 예외가 발생하면 false 반환
            }
        }
        return false;  // id로 Todo를 찾을 수 없으면 false 반환
    }



    public boolean deleteTodo(long id){
        Optional<Todo> todo = todoRepository.findById(id);  // id로 Todo 조회
        if (todo.isPresent()) {
            todoRepository.delete(todo.get());  // Todo 삭제
            return true;
        }
        return false;
    }

    public List<Todo> findAll(){
        return todoRepository.findAll();
    }
}
