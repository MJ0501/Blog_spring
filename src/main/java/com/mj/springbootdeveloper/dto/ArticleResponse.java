package com.mj.springbootdeveloper.dto;

import com.mj.springbootdeveloper.domain.Article;
import lombok.Getter;

// 응답을 위한 DTO .. 제목과 내용으로 구성된 entity를 인수로 받는 생성자 추가함.
@Getter
public class ArticleResponse {
    private final String title;
    private final String content;
    public ArticleResponse(Article article){
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
