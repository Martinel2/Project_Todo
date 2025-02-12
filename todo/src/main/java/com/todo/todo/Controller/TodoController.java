package com.todo.todo.Controller;

import com.todo.todo.Entity.Todo;
import com.todo.todo.Service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // ✅ 할 일 추가
    @PostMapping("/create")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo savedTodo = todoService.createTodo(todo);
        return ResponseEntity.ok(savedTodo);
    }


    @PostMapping("/remove")
    public ResponseEntity<String> removeTodo(@RequestBody Todo todo) {
        boolean isDeleted = todoService.removeTodo(todo);
        if (isDeleted) {
            return ResponseEntity.ok("Todo item successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo item not found");
        }
    }


    // ✅ 할 일 목록 조회
    @GetMapping
    public ResponseEntity<List<Todo>> getTodos() {
        List<Todo> todos = todoService.findAll();
        return ResponseEntity.ok(todos);
    }
}

