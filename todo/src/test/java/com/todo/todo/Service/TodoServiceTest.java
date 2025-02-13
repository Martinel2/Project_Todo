package com.todo.todo.Service;

import com.todo.todo.Dto.TodoCreateDTO;
import com.todo.todo.Dto.TodoDTO;
import com.todo.todo.Entity.Todo;
import com.todo.todo.Entity.User;
import com.todo.todo.Repository.TodoRepository;
import com.todo.todo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Transactional
class TodoServiceTest {
    @Autowired
    private TodoService todoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void testCreateTodoForUser() {
        String email = "user@example.com";
        String provider = "google";
        String task = "Complete the task";

        // Setup test user
        User user = new User();
        user.setUsername("TestUser");
        user.setEmail(email);
        user.setProvider(provider);
        userRepository.save(user);

        TodoCreateDTO todoCreateDTO = new TodoCreateDTO();
        todoCreateDTO.setProvider(provider);
        todoCreateDTO.setEmail(email);
        todoCreateDTO.setTask(task);

        Todo todo = todoService.createTodo(todoCreateDTO);

        assertNotNull(todo);
        assertEquals(task, todo.getTitle());
        assertEquals(user, todo.getUser());
    }

    @Test
    void testGetTodosByUser() {
        String email = "user@example.com";
        String provider = "google";

        // Setup test user
        User user = new User();
        user.setUsername("TestUser");
        user.setEmail(email);
        user.setProvider(provider);
        userRepository.save(user);

        // Add todos for the user
        TodoCreateDTO todoCreateDTO = new TodoCreateDTO();
        todoCreateDTO.setTask("Task 1");
        todoCreateDTO.setProvider(provider);
        todoCreateDTO.setEmail(email);
        todoService.createTodo(todoCreateDTO);


        TodoCreateDTO todoCreateDTO2 = new TodoCreateDTO();
        todoCreateDTO2.setTask("Task 2");
        todoCreateDTO2.setProvider(provider);
        todoCreateDTO2.setEmail(email);
        todoService.createTodo(todoCreateDTO2);

        // Test fetching todos
        List<TodoDTO> todos = todoService.getTodosByUser(email, provider);

        assertEquals(2, todos.size());
        assertTrue(todos.stream().anyMatch(t -> t.getTitle().equals("Task 1")));
        assertTrue(todos.stream().anyMatch(t -> t.getTitle().equals("Task 2")));
    }

    @Test
    void testDelete() {
        String email = "user@example.com";
        String provider = "google";

        // Setup test user
        User user = new User();
        user.setUsername("TestUser");
        user.setEmail(email);
        user.setProvider(provider);
        userRepository.save(user);

        // Add todos for the user
        Todo todo1 = new Todo();
        todo1.setTitle("Task 1");
        todo1.setUser(user);
        todoRepository.save(todo1);

        Todo todo2 = new Todo();
        todo2.setTitle("Task 2");
        todo2.setUser(user);
        todoRepository.save(todo2);

        todoService.deleteTodo(todo1.getId());

        // Test fetching todos
        List<TodoDTO> todos = todoService.getTodosByUser(email, provider);

        assertEquals(1, todos.size());
        assertTrue(!todos.stream().anyMatch(t -> t.getTitle().equals("Task 1")));
        assertTrue(todos.stream().anyMatch(t -> t.getTitle().equals("Task 2")));
    }
}
