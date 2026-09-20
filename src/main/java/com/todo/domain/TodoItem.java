package com.todo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TodoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, length = 40)
    private String title;
    @Column
    private String content;
    @Column(nullable = false)
    private boolean completed = false;
    @CreatedDate
    private LocalDateTime createdDateTime;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TodoStatus status = TodoStatus.ACTIVE;


    public void create(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public void modify(
        String title,
        String content,
        boolean completed,
        TodoStatus status
    ) {
        this.title = title;
        this.content = content;
        this.completed = completed;
        this.status = status;
    }
    public void delete() {
        this.status = TodoStatus.DELETED;
    }
}
