package practice.board.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetBoardResponse {
    private Long id;
    private Long memberId;
    private String memberName;
    private String title;
    private String content;
    private List<String> images;
    private List<CommentDto> comments;

    public GetBoardResponse(Long id, Long memberId, String memberName,String title, String content) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.title = title;
        this.content = content;
    }
}
