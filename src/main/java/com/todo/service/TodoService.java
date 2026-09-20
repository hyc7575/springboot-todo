package com.todo.service;

import com.todo.domain.TodoItem;
import com.todo.domain.TodoStatus;
import com.todo.dto.ModifyTodoRequest;
import com.todo.dto.TodoItemDto;
import com.todo.exception.TodoNotFoundException;
import com.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    @Transactional(readOnly = true)
    public TodoItemDto getTodo(long id) {
        TodoItem todo = todoRepository.findById(id)
            .filter(item -> item.getStatus() != TodoStatus.DELETED)
            .orElseThrow(() -> new TodoNotFoundException(id));

        return TodoItemDto.from(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoItemDto> getTodos() {
        return todoRepository.findAll()
            .stream()
            .filter(todo -> todo.getStatus() != TodoStatus.DELETED)
            .map(TodoItemDto::from)
            .toList();
    }

    @Transactional
    public TodoItemDto createTodo(String title, String content) {
        TodoItem todo = new TodoItem();
        todo.create(title, content);
        TodoItem savedTodoItem = todoRepository.save(todo);

        return TodoItemDto.from(savedTodoItem);
    }

    @Transactional
    public TodoItemDto modifyTodo(long id, ModifyTodoRequest request) {
        TodoItem todo = todoRepository.findById(id)
            .orElseThrow(() -> new TodoNotFoundException(id));
        todo.modify(
            request.getTitle(),
            request.getContent(),
            request.isCompleted(),
            request.getStatus()
        );

        return TodoItemDto.from(todo);
    }

    @Transactional
    public TodoItemDto deleteTodo(long id) {
        TodoItem todo = todoRepository.findById(id)
            .orElseThrow(() -> new TodoNotFoundException(id));
        todo.delete();

        return TodoItemDto.from(todo);
    }
}
