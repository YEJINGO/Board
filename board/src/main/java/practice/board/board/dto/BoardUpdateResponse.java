package practice.board.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardUpdateResponse {

    private String title;
    private String content;
    private List<String> image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    @Builder
    public BoardUpdateResponse(String title, String content, List<String> image, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.modifiedAt = LocalDateTime.now();
    }
}
