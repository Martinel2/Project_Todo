package com.todo.todo.Dto;

import com.todo.todo.Entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor  // ✅ 기본 생성자 추가
@AllArgsConstructor
public class TodoDTO {
    private Long id;
    private String title;
    private boolean completed;

    public TodoDTO(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.completed = todo.isCompleted();
    }
}
