package ru.kostrykinmark.task.model;

import lombok.*;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tasks", schema = "public")
public class Task {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "annotation", nullable = false, length = 250)
    @Size(min = 20, max = 250)
    private String annotation;
    @Column(name = "description", nullable = false, length = 2000)
    @Size(min = 20, max = 2000)
    private String description;
    @Column(name = "status", length = 9)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Column(name = "priority", length = 9)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(name = "comments",
            joinColumns = {@JoinColumn(name = "comment_id")},
            inverseJoinColumns = {@JoinColumn(name = "task_id")})
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;
}
