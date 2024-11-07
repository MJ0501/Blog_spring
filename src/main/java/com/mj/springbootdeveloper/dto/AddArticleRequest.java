package com.mj.springbootdeveloper.dto;

import com.mj.springbootdeveloper.domain.Article;
import com.mj.springbootdeveloper.domain.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {
    @NotNull
    @Size(min=1, max = 10)
    private String title;
    @NotNull
    private String content;

    public Article toEntity(User user){
        return Article.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
