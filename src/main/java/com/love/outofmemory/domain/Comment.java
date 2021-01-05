package com.love.outofmemory.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Integer id;
    private User critic;//critic_id
    private Date comment_time;
    private String comment_content;
    private Blog blog; //blog_id

}
