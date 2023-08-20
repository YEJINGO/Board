package practice.board.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String comment;

    public CommentDto(Long id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
