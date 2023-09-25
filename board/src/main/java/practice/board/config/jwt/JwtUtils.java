package practice.board.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import practice.board.config.auth.LoginMember;
import practice.board.config.auth.LoginMemberDetailService;
import practice.board.entity.Member;
import practice.board.entity.MemberEnum;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static practice.board.config.jwt.JwtProperties.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${jwt.token.access-token-secret-key}")
    private String accessSecretKey;
    @Value("${jwt.token.refresh-token-secret-key}")
    private String refreshSecretKey;
    private Key accessKey;
    private Key refreshKey;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LoginMemberDetailService loginMemberDetailService;


    // secretKey 를 Base64 인코딩해서 받아오는 로직
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(accessSecretKey); //Base64로 인코딩되어 있는 것을, 값을 가져와서(getDecoder()) 디코드하고(decode(secretKey)), byte 배열로 반환
        accessKey = Keys.hmacShaKeyFor(bytes); //반환된 bytes 를 hmacShaKeyFor() 메서드를 사용해서 Key 객체에 넣기

        bytes = Base64.getDecoder().decode(refreshSecretKey); //Base64로 인코딩되어 있는 것을, 값을 가져와서(getDecoder()) 디코드하고(decode(secretKey)), byte 배열로 반환
        refreshKey = Keys.hmacShaKeyFor(bytes); //반환된 bytes 를 hmacShaKeyFor() 메서드를 사용해서 Key 객체에 넣기
    }
    /**
     * 적절한 설정을 통해 Access 토큰을 생성하여 반환
     * @param authentication
     * @return access token
     */
    public String generateAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
//    claims.put("auth", appUserRoles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(SignatureAlgorithm.HS256, accessKey)
                .compact();
    }

    /**
     * 적절한 설정을 통해 Refresh 토큰을 생성하여 반환
     * @param authentication
     * @return refresh token
     */
    public String generateRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());

        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiresIn)
                .signWith(SignatureAlgorithm.HS256, refreshKey)
                .compact();
    }

    // ACCESS TOKEN 파싱하기
    public String parseAccessToken(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_STRING);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");
        }
        return token;
    }

    // REFRESH TOKEN 파싱하기
    public String parseRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(REFRESH_STRING);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");
        }
        return token;
    }

    // ACCESS 토큰 유효성 판단
    public boolean validationAccessToken(String token) throws IOException {
        try {
            Jwts.parserBuilder().setSigningKey(accessSecretKey).build().parseClaimsJws(token); //토큰에 대한 검증 로직
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰 입니다.");
        } catch (MalformedJwtException e) {
            log.info("JWT 토큰이 유효하지 않습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰 입니다.");
        }
        return false;
    }

    //REFRESH 토큰 유효성 판단
    public boolean validationRefreshToken(String token) throws IOException {
        try {
            Jwts.parserBuilder().setSigningKey(refreshSecretKey).build().parseClaimsJws(token); //토큰에 대한 검증 로직
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰 입니다.");
        } catch (MalformedJwtException e) {
            log.info("JWT 토큰이 유효하지 않습니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰 입니다.");
        }
        return false;
    }


    private void setResponse(HttpServletResponse response, Exception e, String errorMessage) throws IOException {
        log.error(errorMessage, e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(errorMessage)));
    }
    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }

    /**
     * Access 토큰으로부터 클레임을 만들고, 이를 통해 User 객체를 생성하여 Authentication 객체를 반환
     * @param accessToken
     * @return
     */
    public Authentication getAuthenticationByAccessToken(String accessToken) {
        String userPrincipal = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(accessToken).getBody().getSubject();
        UserDetails userDetails = loginMemberDetailService.loadUserByUsername(userPrincipal);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Refresh 토큰으로부터 클레임을 만들고, 이를 통해 User 객체를 생성하여 Authentication 객체를 반환
     *
     * @param refreshToken
     * @return
     */
    public Authentication getAuthenticationByRefreshToken(String refreshToken) {
        String userPrincipal = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken).getBody().getSubject();
        UserDetails userDetails = loginMemberDetailService.loadUserByUsername(userPrincipal);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     *
     * @param req
     * @return
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 로그인 유저 생성
    public LoginMember getLoginUser(String token) {
        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long id = claims.get("id", Long.class);
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);
        Member member = Member.builder()
                .id(id)
                .name(name)
                .role(MemberEnum.valueOf(role))
                .build();
        return new LoginMember(member);
    }

}
