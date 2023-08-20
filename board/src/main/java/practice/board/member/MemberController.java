package practice.board.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import practice.board.config.auth.LoginMember;
import practice.board.member.dto.LoginResponse;
import practice.board.member.dto.MemberLoginRequest;
import practice.board.member.dto.MemberSignupRequest;
import practice.board.member.dto.TokenDto;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    // 회원 가입
    @PostMapping("/signup")
    public void signup(@RequestBody MemberSignupRequest request) {
        memberService.signup(request);
    }
    //로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginRequest request) {
        return memberService.login(request);
    }
    //로그아웃
    @GetMapping("/logout")
    public void logout(@AuthenticationPrincipal LoginMember loginMember) {
        log.info(loginMember.getMember().getName() + " 로그아웃 요청");
    }
}
