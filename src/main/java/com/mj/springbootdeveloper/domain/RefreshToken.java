package com.mj.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Column(name="refresh_token", nullable = false)
    private String refreshToken;

    @Column(name="device_info")
    private String deviceInfo;

    @Builder
    public RefreshToken(User user,String refreshToken, String deviceInfo){
        this.user = user;
        this.refreshToken = refreshToken;
        this.deviceInfo = deviceInfo;
    }

    public RefreshToken update(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }
}
