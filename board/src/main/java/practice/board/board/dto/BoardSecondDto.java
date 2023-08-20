package practice.board.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class BoardSecondDto {
    private Long id;
    private String title;
    private String content;
    private List<String> images;
    private List<String> comments;

    public BoardSecondDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
