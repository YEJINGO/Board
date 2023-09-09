package practice.board.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import practice.board.comment.dto.CommentRequest;
import practice.board.comment.dto.CommentResponse;
import practice.board.config.auth.LoginMember;
import practice.board.config.auth.LoginMemberDetailService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/{boardId}")
    @ResponseBody
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long boardId,
                                                         @RequestBody CommentRequest request,
                                                         @AuthenticationPrincipal LoginMember loginMember) {
        return commentService.createComment(boardId, request, loginMember);
    }
}
