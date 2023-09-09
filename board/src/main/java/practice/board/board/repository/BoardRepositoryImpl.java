package practice.board.board.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import practice.board.board.dto.AllBoardResponse;
import practice.board.board.dto.CommentDto;
import practice.board.board.dto.GetBoardResponse;

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
    public Page<AllBoardResponse> findAllBoards(Pageable pageable) {
        List<AllBoardResponse> findAllBoards = queryFactory
                .select(Projections.constructor(AllBoardResponse.class,
                        board.id,
                        board.member.id,
                        board.member.name,
                        board.title,
                        board.content))
                .from(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> findAllBoardsId = findAllBoards.stream().map(AllBoardResponse::getId)
                .collect(Collectors.toList());

        // 53,54번째 줄과 아래 코드는 동일하다
//        List<Long> findAllBoardsId = new ArrayList<>();
//        for (AllBoardResponse response : findAllBoards) {
//            findAllBoardsId.add(response.getId());
//        }
        Map<Long, List<String>> boardImages = queryFactory
                .select(board.id, boardImage.image)
                .from(board)
                .leftJoin(boardImage).on(board.id.eq(boardImage.board.id))
                .where(board.id.in(findAllBoardsId))
                .orderBy(boardImage.id.asc())
                .transform(GroupBy.groupBy(board.id)
                        .as(GroupBy.list(boardImage.image))
                );
        findAllBoards.forEach(r ->
                r.setImages(boardImages.getOrDefault(r.getId(), new ArrayList<>()))
        );
        Map<Long, List<CommentDto>> comments = queryFactory
                .select(board.id, Projections.constructor(CommentDto.class,
                        comment1.member.id,
                        comment1.comment))
                .from(comment1)
                .leftJoin(board).on(comment1.board.id.eq(board.id))
                .where(board.id.in(findAllBoardsId))
                .orderBy(comment1.id.asc())
                .transform(GroupBy.groupBy(board.id)
                        .as(GroupBy.list(Projections.constructor(CommentDto.class,
                                comment1.member.id,
                                comment1.comment)))
                );
        findAllBoards.forEach(r ->
                r.setComments(comments.getOrDefault(r.getId(), new ArrayList<>()))
        );

        Long count = queryFactory.select(board.count())
                .from(board)
                .fetchOne();

        return new PageImpl<AllBoardResponse>(findAllBoards, pageable, count);
    }

    @Override
    public GetBoardResponse findBoard(Long boardId) {
        GetBoardResponse getBoardResponse = queryFactory
                .select(Projections.constructor(GetBoardResponse.class,
                        board.id,
                        board.member.id,
                        board.member.name,
                        board.title,
                        board.content))
                .from(board)
                .where(board.id.eq(boardId))
                .fetchOne();


        List<String> getBoardImages = queryFactory
                .select(boardImage.image)
                .from(boardImage)
                .leftJoin(board).on(board.id.eq(boardImage.board.id))
                .where(board.id.in(boardId))
                .orderBy(boardImage.id.asc())
                .fetch();
        getBoardResponse.setImages(getBoardImages);

        List<CommentDto> getBoardComments = queryFactory
                .select(Projections.constructor(CommentDto.class,
                        comment1.member.id,
                        comment1.comment))
                .from(comment1)
                .leftJoin(board).on(comment1.board.id.eq(board.id))
                .where(board.id.in(boardId))
                .orderBy(comment1.id.asc())
                .fetch();
        getBoardResponse.setComments(getBoardComments);

        return getBoardResponse;
    }
}
