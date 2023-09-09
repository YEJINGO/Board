package practice.board.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponse {

    private String title;
    private String content;
    private List<String> image;
    private LocalDateTime createdAt;

    @Builder
    public BoardResponse(String title, String content, List<String> image, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = LocalDateTime.now();
    }
}
