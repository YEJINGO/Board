package practice.board.member;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.board.config.auth.LoginMember;
import practice.board.config.auth.LoginMemberDetailService;
import practice.board.config.jwt.JwtProperties;
import practice.board.config.jwt.JwtUtils;
import practice.board.entity.Member;
import practice.board.member.dto.LoginResponse;
import practice.board.member.dto.MemberLoginRequest;
import practice.board.member.dto.MemberSignupRequest;
import practice.board.member.dto.TokenDto;

import java.util.concurrent.TimeUnit;

import static practice.board.config.jwt.JwtProperties.REFRESH_TOKEN_EXPIRE_TIME;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    private final JwtUtils jwtUtils;

    // 회원가입
    @Transactional
    public void signup(MemberSignupRequest request) {
        if (memberRepository.existsByName(request.getName())) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        memberRepository.save(member);
    }

    //로그인
    @Transactional
    public ResponseEntity<TokenDto> login(MemberLoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                request.getName(),
                                request.getPassword()
                        )
                );
        String accessToken = jwtUtils.generateAccessToken(authentication);
        String refreshToken = jwtUtils.generateRefreshToken(authentication);
        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);

        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                REFRESH_TOKEN_EXPIRE_TIME,
                TimeUnit.MILLISECONDS
        );
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        httpHeaders.add("RefreshToken", "Bearer " + refreshToken);
        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
    }
}
