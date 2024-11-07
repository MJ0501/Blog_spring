package com.mj.springbootdeveloper.dto;

import com.mj.springbootdeveloper.domain.Article;
import lombok.Getter;

@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    private final String author;

    public ArticleResponse(Article article){
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getUser().getNickname();
    }
}
