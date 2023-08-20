package practice.board.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentRequest {
    private String comment;
    private LocalDateTime createdAt;

    public CommentRequest(String comment) {
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }
}
