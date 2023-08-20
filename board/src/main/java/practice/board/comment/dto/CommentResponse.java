package practice.board.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.board.entity.Board;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponse {
    private String comment;
    private LocalDateTime createdAt;

    public CommentResponse(String comment) {
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }
}
