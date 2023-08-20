package practice.board.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import practice.board.member.dto.FindPassword;
import practice.board.member.dto.FindUserDto;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/member/find")
public class MemberFindController {
    private final MemberFindService memberFindService;

    //메일로 아이디 보내기
    @PostMapping("/username")
    public void sendEmail(@RequestBody FindUserDto findUserDto) {
        memberFindService.sendEmail(findUserDto);
    }

    @PostMapping("/password")
    public void findPassword(@RequestBody FindPassword findPassword) throws Exception {
        memberFindService.findPassword(findPassword);
    }


}
