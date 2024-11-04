package com.mj.springbootdeveloper.dto;

import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AddCommentRequest {
    private Long articleId;
    private String content;

    public Comment toEntity(String author, Article article){
        return Comment.builder()
                .article(article)
                .content(content)
                .author(author)
                .build();
    }
}
