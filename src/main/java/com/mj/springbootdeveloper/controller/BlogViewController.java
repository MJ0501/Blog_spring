package com.mj.springbootdeveloper.controller;

import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.dto.ArticleListViewResponse;
import com.mj.springbootdeveloper.dto.ArticleViewResponse;
import com.mj.springbootdeveloper.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
//lombok annotation. final 필드와 @NonNull 필드만 포함한 생성자를 생성
@RequiredArgsConstructor
public class BlogViewController {
    private final BlogService blogService;
    @GetMapping("/articles")
    //Model(springframework꺼): view(html)쪽으로 값을 넘겨주는 객체.
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = blogService.findAll()
                // stream : collection이나 array와 같은 데이터 소스를 선언적 방식으러 처리하는데 사용됨.
                .stream()
                // ArticleListViewResponse 는 Article객체를 인자로 받는 생성자가 필요함.
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles",articles);
        // articleList.html 라는 view return
        return "articleList";
    }
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model){
        Article article = blogService.findById(id);
        model.addAttribute("article",new ArticleViewResponse(article));
        // article.html 라는 view return
        return "article";
    }
}
