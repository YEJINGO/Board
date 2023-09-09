package practice.board.board.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class BoardTotalResponse {
//    private List<BoardDto> boards;
    private List<AllBoardResponse> boards;

    private int totalPage;
    private int currentPage;



    public BoardTotalResponse(Page<AllBoardResponse> boards) {
        this.boards = boards.getContent();
        this.totalPage = boards.getTotalPages();
        this.currentPage = boards.getNumber();

    }
}
