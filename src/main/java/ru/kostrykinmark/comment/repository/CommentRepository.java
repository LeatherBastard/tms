package ru.kostrykinmark.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kostrykinmark.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
