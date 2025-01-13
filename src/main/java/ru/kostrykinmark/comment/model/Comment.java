package ru.kostrykinmark.comment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "comments")
public class Comment {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User user;
    @Column(name = "text", nullable = false, length = 250)
    private String text;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST,
                    CascadeType.MERGE}, mappedBy = "comments")
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();
}
