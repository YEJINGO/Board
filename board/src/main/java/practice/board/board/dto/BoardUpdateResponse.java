package practice.board.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardUpdateResponse {

    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    @Builder
    public BoardUpdateResponse(String title, String content, String image, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.modifiedAt = LocalDateTime.now();
    }
}
