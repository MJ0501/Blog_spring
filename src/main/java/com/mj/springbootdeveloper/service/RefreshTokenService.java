package com.mj.springbootdeveloper.service;

import com.mj.springbootdeveloper.config.jwt.TokenProvider;
import com.mj.springbootdeveloper.domain.RefreshToken;
import com.mj.springbootdeveloper.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public RefreshToken findByRefreshToken(String refreshToken){
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(()->new IllegalArgumentException("Unexpected token"));
    }
    @Transactional
    public void delete() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = tokenProvider.getUserId(token);
        refreshTokenRepository.deleteByUserId(userId);
    }
}
