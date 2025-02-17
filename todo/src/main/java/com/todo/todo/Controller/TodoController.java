package com.todo.todo.Controller;

import com.todo.todo.Dto.TodoCreateDTO;
import com.todo.todo.Dto.TodoDTO;
import com.todo.todo.Entity.Todo;
import com.todo.todo.Service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (todo != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(todo);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateTodo(@RequestBody TodoDTO todoDTO) {
        boolean isUpdated = todoService.updateTodo(todoDTO);  // id를 통해 삭제 작업 수행
        if (isUpdated) {
            return ResponseEntity.ok(todoDTO.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo item not found");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<TodoDTO>> getTodos(@RequestParam String email, @RequestParam String provider) {
        List<TodoDTO> todos = todoService.getTodosByUser(email, provider);
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteTodo(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");  // id만 받기
        boolean isDeleted = todoService.deleteTodo(id);  // id를 통해 삭제 작업 수행
        if (isDeleted) {
            return ResponseEntity.ok("Todo item successfully deleted");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Todo item not found");
        }
    }


}

