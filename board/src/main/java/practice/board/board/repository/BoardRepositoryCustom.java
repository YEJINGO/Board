package practice.board.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import practice.board.board.dto.BoardDto;
import practice.board.board.dto.BoardSecondDto;

public interface BoardRepositoryCustom {
    Page<BoardSecondDto> findAllBoards(Pageable pageable);

}
