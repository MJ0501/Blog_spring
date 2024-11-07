package com.mj.springbootdeveloper.service;

import com.mj.springbootdeveloper.config.error.exception.ArticleNotFoundException;
import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.domain.Comment;
import com.mj.springbootdeveloper.domain.User;
import com.mj.springbootdeveloper.dto.AddArticleRequest;
import com.mj.springbootdeveloper.dto.AddCommentRequest;
import com.mj.springbootdeveloper.dto.UpdateArticleRequest;
import com.mj.springbootdeveloper.repository.BlogRepository;
import com.mj.springbootdeveloper.repository.CommentRepository;

import com.mj.springbootdeveloper.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private String getAuthenticatedUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getUser().getEmail().equals(userName)) {
            throw new IllegalArgumentException("not Authorized");
        }
    }

    public Article save(AddArticleRequest request,String author) {
        author = getAuthenticatedUserName();
        User user = userRepository.findByEmail(author).orElseThrow(()->new IllegalArgumentException("User not found"));
        return blogRepository.save(request.toEntity(user));
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request, String name) {
        Article article = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());
        return article;
    }

    public void delete(long id, String name) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found :" + id));
        authorizeArticleAuthor(article);
        blogRepository.delete(article);
    }

    public Comment addComment(AddCommentRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()->new IllegalArgumentException("User not found:"+userEmail));
        Article article = blogRepository.findById(request.getArticleId()).orElseThrow(() -> new IllegalArgumentException("not found : " + request.getArticleId()));
        return commentRepository.save(request.toEntity(user, article));
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
    }
}


