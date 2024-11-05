package com.mj.springbootdeveloper.config.error.exception;

import com.mj.springbootdeveloper.config.error.ErrorCode;

public class ArticleNotFoundException extends NotFoundException{
    public ArticleNotFoundException(){
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
