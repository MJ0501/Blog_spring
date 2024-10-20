package com.mj.springbootdeveloper.service;

import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.dto.AddArticleRequest;
import com.mj.springbootdeveloper.dto.UpdateArticleRequest;
import com.mj.springbootdeveloper.repository.BlogRepository;
import jakarta.transaction.Transactional;
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
    public Article findById(long id){
        return blogRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found: "+id));
    }
    public void delete(long id){
        blogRepository.deleteById(id);

    }
    @Transactional
    // 데이터의 무결성과 일관성을 보장하기 위해 트랜잭션 필요함
    // add의 경우 save(), delete의 경우 deleteById 는 그 method에 트랜잭션이 기본 적용되므로 괜찮은데,
    // update의 경우 여러 단계가 필요한 작업이다.
    // 트랜잭션은 원자성(모든 작업이 완료되거나 모두 취소됨)을 보장하므로,
    // 여러 단계의 update가 중간에 오류가 있다면 전부 취소 되게 하여 데이터의 일관성을 유지한다.
    // 만약 delete도 단순히 deleteById 하나만이 아닌 추후 여러 단계가 이어지나면
    // 트랜잭션을 명시적으로 선언(@Transactional)해야한다.
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found: "+id));
        article.update(request.getTitle(),request.getContent());
        return article;
    }
}
