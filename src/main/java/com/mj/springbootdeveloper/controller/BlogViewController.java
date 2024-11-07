package com.mj.springbootdeveloper.controller;

import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.dto.ArticleListViewResponse;
import com.mj.springbootdeveloper.dto.ArticleViewResponse;
import com.mj.springbootdeveloper.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogViewController {
    private final BlogService blogService;
    private String getCurrentUserName(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null && authentication.isAuthenticated()) ? authentication.getName() :  "Anonymous";
    }
    @GetMapping("/articles")
    public String getArticles(Model model){
        List<ArticleListViewResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles",articles);
        model.addAttribute("currentUser",getCurrentUserName());
        return "articleList";
    }
    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable(value="id") Long id, Model model){
        Article article = blogService.findById(id);
        model.addAttribute("article",new ArticleViewResponse(article));
        model.addAttribute("currentUser",getCurrentUserName());
        return "article";
    }
    @GetMapping("/new-article")
    public String newArticle(@RequestParam(value="id", required = false) Long id, Model model){
        if (id == null){
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        model.addAttribute("currentUser",getCurrentUserName());
        return "newArticle";
    }

}
