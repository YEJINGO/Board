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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    // 게시물 전체 조회 api
    @GetMapping
    public BoardTotalResponse boards(Pageable pageable)      {
        return boardService.getBoards(pageable);
    }


    // 특정 게시물 조회 api
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> board(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    // 게시물 작성 api
    @PostMapping
    public ResponseEntity<BoardResponse> create(@AuthenticationPrincipal LoginMember loginMember,
                                                @RequestPart(value = "boardRequest") BoardRequest request,
                                                @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {
        return boardService.create(loginMember, request, multipartFile);
    }

    // 게시물 수정 api
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardUpdateResponse> update(@AuthenticationPrincipal LoginMember loginMember,
                                                      @PathVariable Long boardId,
                                                      @RequestPart(value = "boardUpdateRequest") BoardUpdateRequest request,
                                                      @RequestPart(value = "file") MultipartFile multipartFile) throws IOException {

        return boardService.update(loginMember, boardId, request, multipartFile);
    }
}
