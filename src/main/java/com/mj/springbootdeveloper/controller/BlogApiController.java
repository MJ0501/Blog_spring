package com.mj.springbootdeveloper.controller;

import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.domain.Comment;
import com.mj.springbootdeveloper.dto.*;
import com.mj.springbootdeveloper.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {
    private final BlogService blogService;
    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody @Validated AddArticleRequest request, Principal principal){
        Article savedArticle = blogService.save(request,principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(){
        List<ArticleResponse> articles = blogService.findAll().stream().map(ArticleResponse::new).toList();
        return ResponseEntity.ok().body(articles);
    }
    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable(value="id") long id){

        Article article = blogService.findById(id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable(value="id") long id){
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable(value="id") long id, @RequestBody UpdateArticleRequest request){
        Article updatedArticle = blogService.update(id, request);
        return  ResponseEntity.ok().body(updatedArticle);
    }
    @PostMapping("/api/comments")
    public ResponseEntity<AddCommentResponse> addComment(@RequestBody AddCommentRequest request, Principal principal){
        Comment savedComment = blogService.addComment(request, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new AddCommentResponse(savedComment));
    }
}
