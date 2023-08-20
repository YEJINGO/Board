package practice.board.board.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import practice.board.board.dto.BoardSecondDto;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static practice.board.entity.QBoard.board;
import static practice.board.entity.QBoardImage.boardImage;
import static practice.board.entity.QComment.comment1;

public class BoardRepositoryImpl implements BoardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 게시물 전체 조회 -> 게시물 + 게시물 이미지 + 댓글 +
     * 게시물(Board)
     * boardId, title, content createdAt, modifiedAt, member
     * 게시물 이미지(BoardImage)
     * imageId, image
     * 댓글
     * commentId, comment, createdAt, member
     */
    @Override
    public Page<BoardSecondDto> findAllBoards(Pageable pageable) {
        List<BoardSecondDto> result = queryFactory
                .select(Projections.constructor(BoardSecondDto.class,
                        board.id,
                        board.title,
                        board.content))
                .from(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> resultId = result.stream().map(BoardSecondDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<String>> boardImages = queryFactory
                .select(board.id, boardImage.image)
                .from(board)
                .leftJoin(boardImage).on(board.id.eq(boardImage.board.id))
                .where(board.id.in(resultId))
                .orderBy(boardImage.id.asc())
                .transform(GroupBy.groupBy(board.id)
                        .as(GroupBy.list(boardImage.image))
                );
        result.forEach(r ->
                r.setImages(boardImages.getOrDefault(r.getId(), new ArrayList<>()))
        );
        Map<Long, List<String>> comments = queryFactory
                .select(board.id, comment1.comment)
                .from(board)
                .leftJoin(comment1).on(board.id.eq(comment1.board.id))
                .where(board.id.in(resultId))
                .orderBy(comment1.id.asc())
                .transform(GroupBy.groupBy(board.id)
                        .as(GroupBy.list(comment1.comment))
                );
        result.forEach(r ->
                r.setComments(comments.getOrDefault(r.getId(), new ArrayList<>()))
        );

        Long count = queryFactory.select(board.count())
                .from(board)
                .fetchOne();

        return new PageImpl<BoardSecondDto>(result, pageable, count);
    }
}
