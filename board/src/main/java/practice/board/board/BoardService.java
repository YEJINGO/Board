package practice.board.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import practice.board.board.dto.*;
import practice.board.board.repository.BoardImageRepository;
import practice.board.board.repository.BoardRepository;
import practice.board.comment.CommentRepository;
import practice.board.config.auth.LoginMember;
import practice.board.config.s3.S3Uploader;
import practice.board.entity.Board;
import practice.board.entity.BoardImage;
import practice.board.member.MemberRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final BoardImageRepository boardImageRepository;

    private final S3Uploader s3Uploader;

    public BoardTotalResponse getBoards(Pageable pageable) {

        Page<BoardSecondDto> result = boardRepository.findAllBoards(pageable);
        return new BoardTotalResponse(result);
    }

    public ResponseEntity<BoardResponse> getBoard(Long id) {
        return null;
    }

    @Transactional
    public ResponseEntity<BoardResponse> create(LoginMember loginMember, BoardRequest request, MultipartFile multipartFile) throws IOException {

        String localDate = LocalDate.now().toString();
        String uploadImage = s3Uploader.upload(multipartFile, localDate);

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(loginMember.getMember())
                .build();
        boardRepository.save(board);

        BoardImage boardImage = BoardImage.builder()
                .image(uploadImage)
                .board(board)
                .build();
        boardImageRepository.save(boardImage);

        BoardResponse boardResponse = BoardResponse.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .image(boardImage.getImage())
                .createdAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(boardResponse);
    }

    public ResponseEntity<BoardUpdateResponse> update(LoginMember loginMember, Long boardId, BoardUpdateRequest request, MultipartFile multipartFile) throws IOException {
        String localDate = LocalDate.now().toString();
        String uploadImage = s3Uploader.upload(multipartFile, localDate);

        Board findBoard = boardRepository.findById(boardId).orElseThrow();

        boardRepository.findByMemberId(loginMember.getMember().getId());
        findBoard.setTitle((request.getTitle()));
        findBoard.setContent(request.getContent());
        boardRepository.save(findBoard);

        BoardImage findImage = boardImageRepository.findByBoardId(boardId);
        findImage.setImage(uploadImage);
        boardImageRepository.save(findImage);

        BoardUpdateResponse boardUpdateResponse = BoardUpdateResponse.builder()
                .title(findBoard.getTitle())
                .content(findBoard.getContent())
                .image(findImage.getImage())
                .createdAt(findBoard.getCreatedAt())
                .modifiedAt(findBoard.getModifiedAt())
                .build();

        return ResponseEntity.ok(boardUpdateResponse);
    }

}
