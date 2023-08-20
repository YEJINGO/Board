package practice.board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    @Override
    List<Board> findAll();

    void findByMemberId(Long id);
}
