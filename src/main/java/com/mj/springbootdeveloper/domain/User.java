package com.mj.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="nickname", unique = true)
    private String nickname;

    @Column(name="is_oauth_user", nullable = false)
    private boolean isOAuthUser;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> refreshTokens= new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname, boolean isOAuthUser){
        this.email = email;
        this.password = password;
        this.nickname = (nickname != null && !nickname.isEmpty()) ? nickname : email.split("@")[0];
        this.isOAuthUser = isOAuthUser;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
// 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        return true;
    }

    public User update(String nickname) {
        this.nickname = nickname;
        return this;
    }
}
