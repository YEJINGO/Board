package practice.board.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import practice.board.config.auth.LoginMember;
import practice.board.member.dto.ChangePasswordRequest;
import practice.board.member.dto.FindPassword;
import practice.board.member.dto.FindUserDto;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/member/find")
public class MemberFindController {
    private final MemberFindService memberFindService;

    //메일로 아이디 보내기
    @PostMapping("/username")
    @ResponseStatus(code= HttpStatus.OK)
    public void sendEmail(@RequestBody FindUserDto findUserDto) {
        memberFindService.sendEmail(findUserDto);
    }

    @PostMapping("/password")
    @ResponseStatus(code= HttpStatus.OK)
    public void findPassword(@RequestBody FindPassword findPassword) throws Exception {
        memberFindService.findPassword(findPassword);
    }

    // 비밀번호 바꾸기
    @PostMapping("/changePassword")
    @ResponseStatus(code= HttpStatus.OK)
    public void changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal LoginMember loginMember) {
        memberFindService.changePassword(changePasswordRequest, loginMember);
    }

}
