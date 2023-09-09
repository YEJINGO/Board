package practice.board.member;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import practice.board.common.GlobalException;
import practice.board.config.auth.LoginMember;
import practice.board.entity.Member;
import practice.board.member.dto.ChangePasswordRequest;
import practice.board.member.dto.FindPassword;
import practice.board.member.dto.FindUserDto;

@Service
@RequiredArgsConstructor
public class MemberFindService {

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void sendEmail(FindUserDto findUserDto) {
        String setEmail = "yaejingo@naver.com";
        String email = findUserDto.getEmail();

        Member findMember = memberRepository.findByEmail(email);
        try {
            if (findMember != null) {
                String username = findMember.getName();
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setTo(email);
                simpleMailMessage.setSubject("아이디 찾기");
                simpleMailMessage.setText("가입하신 아이디는 " + username + "입니다.");
                simpleMailMessage.setFrom(setEmail);
                mailSender.send(simpleMailMessage);
            } else {
                throw new Exception("이메일이 존재하지 않습니다.");
            }
        } catch (Exception e) {
            // 예외 발생 시 처리 로직
            e.printStackTrace(); // 혹은 로깅 등을 수행
        }
        System.out.println("끝");
    }

    public void findPassword(FindPassword findPassword) throws Exception {
        String setEmail = "yaejingo@naver.com";
        String name = findPassword.getName();
        Member findMember = memberRepository.findByName(name).orElseThrow();

        if (findMember != null) {
            // 값이 존재하는 경우 처리
            String str = getTempPassword();
            findMember.setPassword(bCryptPasswordEncoder.encode(str));
            memberRepository.save(findMember);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(findMember.getEmail());
            simpleMailMessage.setSubject("임시 비밀번호 안내 이메일 입니다.");
            simpleMailMessage.setText("안녕하세요. 임시 비밀번호 안내 관련 이메일 입니다.\n" + "회원님의 임시 비밀번호는 " + str + " 입니다. \n" + "로그인 후 비밀 변호를 변경해주세요.");
            simpleMailMessage.setFrom(setEmail);
            mailSender.send(simpleMailMessage);

        } else {
            throw new Exception("아이디가 존재하지 않습니다.");
        }
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest, LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getMember().getEmail());

        String newPassword = changePasswordRequest.getNewPassword();
        String currentPassword = changePasswordRequest.getCurrentPassword();
        String registeredPassword = member.getPassword();

        if (bCryptPasswordEncoder.matches(currentPassword, registeredPassword)) {
            member.passwordUpdate(bCryptPasswordEncoder.encode(newPassword));
            memberRepository.save(member);
        } else {
            throw new GlobalException("현재 비밀번호가 일치하지 않습니다");
        }
        System.out.println("비밀번호 변경완료");
    }

    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
}
