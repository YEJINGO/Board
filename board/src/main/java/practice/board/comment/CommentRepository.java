package practice.board.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.board.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
