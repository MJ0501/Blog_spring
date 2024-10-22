package com.mj.springbootdeveloper.service;

import com.mj.springbootdeveloper.config.jwt.TokenProvider;
import com.mj.springbootdeveloper.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken) {
        // RefreshToken 유효성 검증
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalArgumentException("Unexpected token");
        }
        // RefreshToken으로부터 userId가져옴
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        // 사용자 정보 조회
        User user = userService.findById(userId);
        // 새로운 Access token 생성(2H만 유효)
        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
