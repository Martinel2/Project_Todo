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
    @PostMapping("/todo")
    public ResponseEntity<Todo> createTodo(@RequestParam String email, @RequestParam String provider, @RequestParam String task) {
        Todo todo = todoService.createTodo(email, provider, task);
        if (todo != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(todo);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/user")
    public ResponseEntity<List<Todo>> getTodos(@RequestParam String email, @RequestParam String provider) {
        List<Todo> todos = todoService.getTodosByUser(email, provider);
        return ResponseEntity.ok(todos);
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

}

