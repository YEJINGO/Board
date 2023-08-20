package practice.board.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardUpdateRequest {

    private String title;
    private String content;
    private LocalDateTime modifiedAt;

    public BoardUpdateRequest(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }

}
