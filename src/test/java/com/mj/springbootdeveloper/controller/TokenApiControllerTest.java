package com.mj.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.springbootdeveloper.config.jwt.JwtFactory;
import com.mj.springbootdeveloper.config.jwt.JwtProperties;
import com.mj.springbootdeveloper.domain.RefreshToken;
import com.mj.springbootdeveloper.domain.User;
import com.mj.springbootdeveloper.dto.CreateAccessTokenRequest;
import com.mj.springbootdeveloper.repository.RefreshTokenRepository;
import com.mj.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class TokenApiControllerTest {
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void MockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }
//    @BeforeEach
//    void setSecurityContext() {
//        user = userRepository.save(User.builder()
//                .email("user@gmail.com")
//                .password("test")
//                .build());
//
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
//    }

//    @DisplayName("deleteRefreshToken")
//    @Test
//    public void deleteRefreshToken() throws Exception {
//        // given
//        final String url = "/api/refresh-token";
//        String refreshToken = createRefreshToken();
//        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));
//
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, refreshToken, user.getAuthorities()));
//
//        // when
//        ResultActions resultActions = mockMvc.perform(delete(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE));
//
//        // then
//        resultActions.andExpect(status().isOk());
//        assertThat(refreshTokenRepository.findByRefreshToken(refreshToken)).isEmpty();
//    }
    @DisplayName("createNewAccessToken")
    @Test
    public void createNewAccessToken() throws Exception{
        //given
        final String url="/api/token";
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());
        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", testUser.getId()))
                .build()
                .createToken(jwtProperties);
        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));
        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.setRefreshToken(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);
        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));
        //then
        result.andExpect(status().isCreated()).andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

}