package practice.board.board.dto;

import lombok.Getter;

@Getter
public class BoardImageDto {
    private Long id;
    private String comment;

    public BoardImageDto(Long id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
