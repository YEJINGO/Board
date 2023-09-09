package practice.board.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import practice.board.board.dto.*;
import practice.board.config.auth.LoginMember;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    // 게시물 전체 조회 api
    @GetMapping
    public BoardTotalResponse boards(Pageable pageable) {
        return boardService.getBoards(pageable);
    }


    // 특정 게시물 조회 api
    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponse> board(@PathVariable Long boardId) {
        return boardService.getBoard(boardId);
    }

    // 게시물 작성 api
    @PostMapping
    public ResponseEntity<BoardResponse> create(@AuthenticationPrincipal LoginMember loginMember,
                                                @RequestPart(value = "boardRequest") BoardRequest request,
                                                @RequestPart(value = "file") List<MultipartFile> multipartFiles) throws IOException {
        return boardService.create(loginMember, request, multipartFiles);
    }

    // 게시물 수정 api
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardUpdateResponse> update(@AuthenticationPrincipal LoginMember loginMember,
                                                      @PathVariable Long boardId,
                                                      @RequestPart(value = "boardUpdateRequest") BoardUpdateRequest request,
                                                      @RequestPart(value = "file") List<MultipartFile> multipartFiles) throws IOException {

        return boardService.update(loginMember, boardId, request, multipartFiles);
    }
    // 게시물 삭제
    @DeleteMapping("/{boardId}")
    public void delete(@AuthenticationPrincipal LoginMember loginMember,
                       @PathVariable Long boardId) {
        boardService.delete(loginMember, boardId);
    }
}
