package practice.board.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import practice.board.entity.Member;
import practice.board.member.MemberRepository;

/**
 * 시큐리티 설정에서 loginProcessingUrl("/login")으로 걸어 놓은 상태에서,
 * login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행된다.
 */
@Service
@RequiredArgsConstructor
public class LoginMemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. "));
        return new LoginMember(member);

    }
}
