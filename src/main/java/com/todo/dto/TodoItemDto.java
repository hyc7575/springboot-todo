package com.todo.dto;

import com.todo.domain.TodoItem;
import com.todo.domain.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemDto {
    private long id;
    private String title;
    private String content;
    private boolean completed;
    private LocalDateTime createdDateTime;
    private TodoStatus status;

    public static TodoItemDto from(TodoItem todo) {
        return new TodoItemDto(
            todo.getId(),
            todo.getTitle(),
            todo.getContent(),
            todo.isCompleted(),
            todo.getCreatedDateTime(),
            todo.getStatus()
        );
    }
}
