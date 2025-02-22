package com.todo.todo.Controller;

import com.todo.todo.Dto.TodoCreateDTO;
import com.todo.todo.Dto.TodoDTO;
import com.todo.todo.Entity.Todo;
import com.todo.todo.Entity.User;
import com.todo.todo.Repository.UserRepository;
import com.todo.todo.Service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

public class TodoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    void testCreateTodo() throws Exception {
        // Given
        String username = "testUser";
        String email = "test@example.com";
        String provider = "google";

        String task = "Test Todo";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setProvider(provider);
        userRepository.save(user);


        Todo todo = new Todo();
        todo.setTitle(task);
        todo.setUser(user);

        TodoCreateDTO todoCreateDTO = new TodoCreateDTO();
        todoCreateDTO.setTask(task);
        todoCreateDTO.setProvider(provider);
        todoCreateDTO.setEmail(email);

        when(todoService.createTodo(any(TodoCreateDTO.class))).thenReturn(todo);

        // When & Then
        mockMvc.perform(post("/api/todos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test Todo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }

    @Test
    void testUpdateTodo() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/todos/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"content\":\"Updated Todo\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Updated Todo"));
    }

    @Test
    void testGetTodos() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/todos/user")
                        .param("email", "test@example.com")
                        .param("provider", "google"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test Todo"));
    }

    @Test
    void testDeleteTodo() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/todos/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Todo item successfully deleted"));
    }

    @Test
    void testDeleteTodoNotFound() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/todos/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo item not found"));
    }
}

