package ru.kostrykinmark.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostrykinmark.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByTask_Id(int taskId);
}
