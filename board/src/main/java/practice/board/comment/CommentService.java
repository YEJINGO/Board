package practice.board.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.board.board.repository.BoardRepository;
import practice.board.comment.dto.CommentRequest;
import practice.board.comment.dto.CommentResponse;
import practice.board.config.auth.LoginMember;
import practice.board.entity.Board;
import practice.board.entity.Comment;
import practice.board.entity.Member;
import practice.board.member.MemberRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity<CommentResponse> createComment(Long boardId, CommentRequest request, LoginMember loginMember) {
        Member findMember = memberRepository.findById(loginMember.getMember().getId()).orElseThrow();

        Board findBoard = boardRepository.findById(boardId).orElseThrow();

        Comment comment = Comment.builder()
                .comment(request.getComment())
                .board(findBoard)
                .member(findMember)
                .build();
        commentRepository.save(comment);

        CommentResponse commentResponse = new CommentResponse(request.getComment());
        return ResponseEntity.ok(commentResponse);
    }
}
