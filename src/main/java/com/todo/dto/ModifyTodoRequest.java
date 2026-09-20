package com.todo.dto;

import com.todo.domain.TodoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyTodoRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 40, message = "제목은 40자 이하여야 합니다.")
    private String title;

    private String content;
    private boolean completed;
    private TodoStatus status;
}
