package ru.kostrykinmark.task.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.user.model.User;

import java.util.List;


@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tasks", schema = "public")
public class Task {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title", nullable = false, length = 250)
    @Size(min = 5, max = 250)
    private String title;
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
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "task_id")})
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;
}
