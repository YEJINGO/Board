package practice.board.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import practice.board.board.dto.AllBoardResponse;
import practice.board.board.dto.GetBoardResponse;

public interface BoardRepositoryCustom {
    Page<AllBoardResponse> findAllBoards(Pageable pageable);

    GetBoardResponse findBoard(Long boardId);
}
