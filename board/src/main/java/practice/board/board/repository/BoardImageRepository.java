package practice.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.board.entity.BoardImage;

import java.util.List;
import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImage,Long> {
    List<String> findByBoardId(Long boardId);
}
