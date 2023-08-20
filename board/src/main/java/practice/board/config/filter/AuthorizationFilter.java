package practice.board.config.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import practice.board.config.jwt.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtils.parseAccessToken(request);
        String refreshToken = jwtUtils.parseRefreshToken(request);

        if (accessToken != null && refreshToken != null) {
            if (jwtUtils.validationAccessToken(accessToken)) {
                Authentication auth = jwtUtils.getAuthenticationByAccessToken(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else if (!jwtUtils.validationAccessToken(accessToken) && jwtUtils.validationRefreshToken(refreshToken)) {
                Authentication auth = jwtUtils.getAuthenticationByRefreshToken(refreshToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}

