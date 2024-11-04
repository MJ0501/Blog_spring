package com.mj.springbootdeveloper.service;

import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.domain.Comment;
import com.mj.springbootdeveloper.dto.AddArticleRequest;
import com.mj.springbootdeveloper.dto.AddCommentRequest;
import com.mj.springbootdeveloper.dto.UpdateArticleRequest;
import com.mj.springbootdeveloper.repository.BlogRepository;
import com.mj.springbootdeveloper.repository.CommentRepository;
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

    public Article save(AddArticleRequest request, String author){
        return blogRepository.save(request.toEntity(author));
    }
    public List<Article> findAll(){
        return blogRepository.findAll();
    }
    public Article findById(long id){
        return blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found: "+id));
    }
    public void delete(long id){
        Article article = blogRepository.findById(id)
                        .orElseThrow(()->new IllegalArgumentException("not found :"+id));
        authorizeArticleAuthor(article);
        blogRepository.delete(article);

    }

    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!article.getAuthor().equals(userName)){
            throw new IllegalArgumentException("not Authorized");
        }
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found: "+id));
        authorizeArticleAuthor(article);
        article.update(request.getTitle(),request.getContent());
        return article;
    }
    public Comment addComment(AddCommentRequest request, String userName){
        Article article = blogRepository.findById(request.getArticleId()).orElseThrow(()->new IllegalArgumentException("not found : "+request.getArticleId()));
        return commentRepository.save(request.toEntity(userName, article));
    }
}
