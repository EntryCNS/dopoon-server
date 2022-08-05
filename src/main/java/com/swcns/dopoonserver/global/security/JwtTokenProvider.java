package com.swcns.dopoonserver.global.security;

import com.swcns.dopoonserver.global.security.auth.AuthDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String TOKEN_SPLITTER = "Bearer ";

    private final JwtProperties jwtProperties;
    private final AuthDetailsService authDetailsService;

    public String generateAccessToken(String id) {
        return generateToken(id, jwtProperties.getSecretKey(), jwtProperties.getAccessExp());
    }

    // 외부 인프라용 단기간 토큰 발급
    public String generateExternalApiToken(String id) {
        return generateToken(id, jwtProperties.getSecretKey(), 60 * 3L); // 3분 제한시간 부여
    }

    public String generateRefreshToken(String id) {
        return generateToken(id, jwtProperties.getSecretKey(), jwtProperties.getRefreshExp());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith(TOKEN_SPLITTER)) {
            return bearerToken.replace(TOKEN_SPLITTER, "");
        }
        return null;
    }

    public Authentication authentication(String token) {
        UserDetails userDetails = authDetailsService
                .loadUserByUsername(getTokenSubject(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String parseToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(jwtProperties.getSecretKey()))
            return bearerToken.replace(jwtProperties.getSecretKey(), "");
        return null;
    }

    private Claims getTokenBody(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token).getBody();
        } catch (RuntimeException e) {
            throw new RuntimeException("Token body Exception");
        }
    }

    private String getTokenSubject(String token) {
        return getTokenBody(token).getSubject();
    }

    private String generateToken(String id, String type, Long exp) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .setSubject(id)
                .claim("type", type)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp * 1000))
                .compact();
    }

    public LocalDateTime getExpiredTime() {
        return LocalDateTime.now().plusSeconds(jwtProperties.getAccessExp());
    }
}
