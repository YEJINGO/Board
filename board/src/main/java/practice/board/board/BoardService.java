package practice.board.board;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import practice.board.common.GlobalException;
import practice.board.config.auth.LoginMember;
import practice.board.config.s3.S3UploaderImages;
import practice.board.entity.Board;
import practice.board.entity.BoardImage;
import practice.board.member.MemberRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    @Value("${cloud.aws.s3.bucket.name}")
    public String bucket;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final AmazonS3Client amazonS3Client;
    private final S3UploaderImages s3UploaderImages;

    public BoardTotalResponse getBoards(Pageable pageable) {

        Page<AllBoardResponse> result = boardRepository.findAllBoards(pageable);
        return new BoardTotalResponse(result);
    }

    public ResponseEntity<GetBoardResponse> getBoard(Long boardId) {
        GetBoardResponse getBoard = boardRepository.findBoard(boardId);
        return ResponseEntity.ok(getBoard);
    }

    @Transactional
    public ResponseEntity<BoardResponse> create(LoginMember loginMember, BoardRequest request, List<MultipartFile> multipartFiles) throws IOException {

        Board board = Board.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(loginMember.getMember())
                .build();
        boardRepository.save(board);

        List<String> imagesPath = s3UploaderImages.uploadFile(multipartFiles);
        for (String imageUrl : imagesPath) {
            BoardImage boardImage = BoardImage.builder()
                    .image(imageUrl)
                    .imageKey(amazonS3Client.getUrl(bucket, imageUrl).toExternalForm())
                    .board(board)
                    .build();
            boardImageRepository.save(boardImage);
        }

        BoardResponse boardResponse = BoardResponse.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .image(imagesPath)
                .createdAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(boardResponse);
    }

    @Transactional
    public ResponseEntity<BoardUpdateResponse> update(LoginMember loginMember, Long boardId, BoardUpdateRequest request, List<MultipartFile> multipartFiles) throws IOException {
        Board findBoard = boardRepository.findById(boardId).orElseThrow();
        List<String> newImages = null;

        if (loginMember.getMember().getId() != findBoard.getMember().getId()) {
            throw new GlobalException("수정 권한이 없습니다");
        }

        findBoard.update(request.getTitle(), request.getContent());
        boardRepository.save(findBoard);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (BoardImage oldImages : findBoard.getBoardImage()) {
                String url = oldImages.getImageKey();
                s3UploaderImages.deleteFile(url);
            }
            boardImageRepository.deleteAllInBatch(findBoard.getBoardImage());

            newImages = s3UploaderImages.uploadFile(multipartFiles);
            for (String newImage : newImages) {
                BoardImage boardImage = BoardImage.builder()
                        .image(newImage)
                        .imageKey(amazonS3Client.getUrl(bucket, newImage).toExternalForm())
                        .board(findBoard)
                        .build();
                boardImageRepository.save(boardImage);
            }
        }

        BoardUpdateResponse boardUpdateResponse = BoardUpdateResponse.builder()
                .title(findBoard.getTitle())
                .content(findBoard.getContent())
                .image(newImages)
                .createdAt(findBoard.getCreatedAt())
                .modifiedAt(findBoard.getModifiedAt())
                .build();
        return ResponseEntity.ok(boardUpdateResponse);
    }

    @Transactional
    public void delete(LoginMember loginMember, Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new GlobalException("해당하는 게시물이 없습니다."));

        if (loginMember.getMember().getId() != findBoard.getMember().getId()) {
            throw new GlobalException("삭제 권한이 없습니다");
        } else {
            for (BoardImage image : findBoard.getBoardImage()) {
                s3UploaderImages.deleteFile(image.getImageKey());
            }
            boardRepository.delete(findBoard);
        }
    }
}
