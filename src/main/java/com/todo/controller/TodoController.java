package com.todo.controller;

import com.todo.global.response.ApiResponse;
import com.todo.dto.CreateTodoRequest;
import com.todo.dto.ModifyTodoRequest;
import com.todo.dto.TodoItemDto;
import com.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<TodoItemDto>>> getTodos() {
        List<TodoItemDto> todos = todoService.getTodos();
        return ResponseEntity.ok(ApiResponse.of(todos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoItemDto>> getTodo(@PathVariable long id) {
        TodoItemDto todo = todoService.getTodo(id);
        return ResponseEntity.ok(ApiResponse.of(todo));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TodoItemDto>> createTodo(
        @Valid @RequestBody CreateTodoRequest request
    ) {
        TodoItemDto todo = todoService.createTodo(request.getTitle(), request.getContent());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.of(todo));
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<ApiResponse<TodoItemDto>> modifyTodo(
        @PathVariable long id,
        @Valid @RequestBody ModifyTodoRequest request
    ) {
        TodoItemDto todo = todoService.modifyTodo(id, request);
        return ResponseEntity.ok(ApiResponse.of(todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoItemDto>> deleteTodo(
        @PathVariable long id
    ) {
        TodoItemDto todo = todoService.deleteTodo(id);
        return ResponseEntity.ok(ApiResponse.of(todo));
    }
}
