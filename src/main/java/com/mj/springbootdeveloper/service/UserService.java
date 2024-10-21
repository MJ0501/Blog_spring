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
    public Long save(AddUserRequest dto){
        return userRepository.save(User.builder().email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .build()).getId();
    }
}
