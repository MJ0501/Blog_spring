package com.mj.springbootdeveloper.service;

import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.dto.AddArticleRequest;
import com.mj.springbootdeveloper.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {
    private final BlogRepository blogRepository;
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }
    public List<Article> findAll(){
        return blogRepository.findAll();
    }
}
