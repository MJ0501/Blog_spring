package com.mj.springbootdeveloper.dto;

import com.mj.springbootdeveloper.domain.Article;
//import lombok.AllArgsConstructor;
import lombok.Getter;
//@AllArgsConstructor
@Getter
public class ArticleListViewResponse {
    private final Long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article){
        this.id = article.getId();
        this.title=article.getTitle();
        this.content=article.getContent();
    }
}
