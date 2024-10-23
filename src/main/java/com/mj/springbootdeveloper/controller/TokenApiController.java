package com.mj.springbootdeveloper.controller;

import com.mj.springbootdeveloper.dto.CreateAccessTokenRequest;
import com.mj.springbootdeveloper.dto.CreateAccessTokenResponse;
import com.mj.springbootdeveloper.service.RefreshTokenService;
import com.mj.springbootdeveloper.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenApiController {
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request){
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }
    @DeleteMapping("/api/refresh-token")
    public ResponseEntity<?> deleteRefreshToken(){
        refreshTokenService.delete();
        return ResponseEntity.ok().build();
    }
}
