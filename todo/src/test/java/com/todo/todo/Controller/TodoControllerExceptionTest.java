package com.todo.todo.Controller;

import com.todo.todo.Dto.TodoCreateDTO;
import com.todo.todo.Dto.TodoDTO;
import com.todo.todo.Service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private TodoService todoService;

    private TodoDTO todoDTO;

    @BeforeEach
    void setUp() {
        // 초기화
        todoDTO = new TodoDTO();
        todoDTO.setId(1L);
        todoDTO.setContent("Updated Todo");
    }

    // 1. Todo 업데이트 실패 예외 테스트 (존재하지 않는 Todo ID)
    @Test
    void testUpdateTodoNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Todo item not found with id: 1")).when(todoService).updateTodo(any(TodoDTO.class));

        mockMvc.perform(post("/api/todos/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo item not found with id: 1"));
    }

    // 2. Todo 삭제 실패 예외 테스트 (존재하지 않는 Todo ID)
    @Test
    void testDeleteTodoNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Todo item not found with id: 1")).when(todoService).deleteTodo(anyLong());

        mockMvc.perform(post("/api/todos/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Todo item not found with id: 1"));
    }

    // 3. 잘못된 요청 (존재하지 않는 사용자)
    @Test
    void testCreateTodoBadRequest() throws Exception {

        //No User

        TodoCreateDTO badTodoCreateDTO = new TodoCreateDTO();
        badTodoCreateDTO.setContent("create");
        badTodoCreateDTO.setEmail("test@example.com");
        badTodoCreateDTO.setProvider("google");

        mockMvc.perform(post("/api/todos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badTodoCreateDTO)))
                .andExpect(status().isNotFound());
    }
}
