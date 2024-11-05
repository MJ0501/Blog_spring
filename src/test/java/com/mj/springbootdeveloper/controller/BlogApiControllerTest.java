package com.mj.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mj.springbootdeveloper.config.error.ErrorCode;
import com.mj.springbootdeveloper.domain.Comment;
import com.mj.springbootdeveloper.dto.AddCommentRequest;
import com.mj.springbootdeveloper.repository.CommentRepository;
import net.datafaker.Faker;
import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.domain.User;
import com.mj.springbootdeveloper.dto.AddArticleRequest;
import com.mj.springbootdeveloper.dto.UpdateArticleRequest;
import com.mj.springbootdeveloper.repository.BlogRepository;
import com.mj.springbootdeveloper.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    BlogRepository blogRepository;
    @Autowired
    UserRepository userRepository;
    User user;
    @Autowired
    CommentRepository commentRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext(){
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());
        SecurityContext context= SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,user.getPassword(), user.getAuthorities()));
    }

    @DisplayName("addArticle")
    @Test
    public void addArticle() throws Exception {
        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");
        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));
        //then
        result.andExpect(status().isCreated());
        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAllArticles")
    @Test
    public void findAllArticles() throws Exception {
        //given
        final String url = "/api/articles";
        Article savedArticle = createDefaultArticle();
        //when
        final ResultActions resultActions = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build());
    }

    @DisplayName("findArticle")
    @Test
    public void findArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();
        //when
        final ResultActions resultActions = mockMvc.perform(get(url,savedArticle.getId()));
        //then
        resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }
    @DisplayName("deleteArticle")
    @Test
    public void deleteArticle() throws Exception{
        //given
        final String url = "/api/articles/{id}";
        Article savedArticle = createDefaultArticle();
        //when
        mockMvc.perform(delete(url,savedArticle.getId())).andExpect(status().isOk());
        //then
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();
    }
    @DisplayName("updateArticle")
    @Test
    public void updateArticle() throws Exception{
        //given
        final String url="/api/articles/{id}";
        Article savedArticle = createDefaultArticle();

        final String newTitle="new title";
        final String newContent="new content";
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle,newContent);
        //when
        ResultActions result = mockMvc.perform(put(url,savedArticle.getId()).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));
        //then
        result.andExpect(status().isOk());
        Article article = blogRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
    @DisplayName("addArticle:title==null -> fail")
    @Test
    public void addArticleNullValidation() throws Exception{
        //given
        final String url="/api/articles";
        final String title = null;
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");
        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));
        //then
        result.andExpect(status().isBadRequest());
    }
    @DisplayName("addArticle:SIZE(title)>10 -> fail")
    @Test
    public void addArticleSizeValidation() throws Exception{
        Faker faker = new Faker();
        final String url="/api/articles";
        final String title=faker.lorem().characters(11);
        final String content="content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");
        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));
        //then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("addComment")
    @Test
    public void addComment() throws Exception{
        //given
        final String url="/api/comments";
        Article savedArticle = createDefaultArticle();
        final Long articleId = savedArticle.getId();
        final String content = "content";
        final AddCommentRequest userRequest = new AddCommentRequest(articleId, content);
        final String requestBody = objectMapper.writeValueAsString(userRequest);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");
        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));
        //then
        result.andExpect(status().isCreated());
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getArticle().getId()).isEqualTo(articleId);
        assertThat(comments.get(0).getContent()).isEqualTo(content);

    }
    @DisplayName("잘못된HTTP메서드로 아티클 조회시 실패")
    @Test
    public void invalidHttpMethod() throws Exception{
        //given
        final String url ="/api/articles/{id}";
        final long invalidId = 1;

        //when
        final ResultActions result = mockMvc.perform(get(url,invalidId));
        //then
        result.andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.ARTICLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value(ErrorCode.ARTICLE_NOT_FOUND.getCode()));
    }
}




