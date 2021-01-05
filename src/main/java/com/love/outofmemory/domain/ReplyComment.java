package com.love.outofmemory.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyComment {
    private Integer id;
    private Comment comment;//comment_id
    private Date comment_time;
    private String comment_content;



}
