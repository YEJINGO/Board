package practice.board.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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

@Api(tags = {"유저 API 정보를 제공하는 Controller"})
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "회원가입",notes = "회원가입 필수" )
    @GetMapping("/posts")
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
