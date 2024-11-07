package com.mj.springbootdeveloper.service;

import com.mj.springbootdeveloper.dto.AddUserRequest;
import com.mj.springbootdeveloper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.mj.springbootdeveloper.domain.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(AddUserRequest dto, boolean isOAuthUser,String encodedPassword){
        encodedPassword = isOAuthUser ? null : bCryptPasswordEncoder.encode(dto.getPassword());
        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .isOAuthUser(isOAuthUser)
                .build()).getId();
    }
    public User findById(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException("Unexpected user"));
    }
}
