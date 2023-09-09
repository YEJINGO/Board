package practice.board.board.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private List<BoardImageDto> boardImage;
    private List<CommentDto> comment;

    public BoardDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}