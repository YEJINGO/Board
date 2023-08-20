package practice.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.board.entity.BoardImage;

import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage,Long> {
    BoardImage findByBoardId(Long boardId);
}
