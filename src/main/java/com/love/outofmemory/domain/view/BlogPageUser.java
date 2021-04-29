package com.love.outofmemory.domain.view;

import com.love.outofmemory.domain.BlogTag;
import com.love.outofmemory.domain.Classification;
import com.love.outofmemory.domain.User;
import lombok.Data;

import java.util.Date;

@Data
public class BlogPageUser {

    //用户信息
    private Integer userviews;
    private Integer userblogcount;
    private  Integer usercomments;
    private Integer usergoodcounts;
    private Integer usercollections;
    private Integer codeage;
    private Integer befollowed; //若用户登录判断是否被关注



}
