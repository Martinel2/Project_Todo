package com.todo.todo.Controller;

import com.todo.todo.Dto.TodoCreateDTO;
import com.todo.todo.Dto.TodoDTO;
import com.todo.todo.Entity.Todo;
import com.todo.todo.Service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // ✅ 할 일 추가
    @PostMapping("/create")
    public ResponseEntity<Todo> createTodo(@RequestBody TodoCreateDTO todoCreateDTO) {
        Todo todo = todoService.createTodo(todoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo); // 예외 발생 시 자동 처리
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateTodo(@RequestBody TodoDTO todoDTO) {
        todoService.updateTodo(todoDTO);
        return ResponseEntity.ok("Todo item successfully updated");  // 성공 시 OK 응답
    }

    @GetMapping("/user")
    public ResponseEntity<List<TodoDTO>> getTodos(@RequestParam String email, @RequestParam String provider) {
        List<TodoDTO> todos = new ArrayList<>();
        if(todoService.getTodosByUser(email, provider) != null) todos = todoService.getTodosByUser(email, provider);
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteTodo(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");  // id만 받기
        todoService.deleteTodo(id);   // 삭제 실패 시 예외 발생

        return ResponseEntity.ok("Todo item successfully deleted");  // 성공 시 OK 응답
    }



}

