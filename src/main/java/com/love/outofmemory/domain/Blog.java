package com.love.outofmemory.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Blog {
    private Integer id;
    //user_id
    private User user;
    private Date publish_time;
    //classification
    private Classification classification;
    private Integer good_count;
    private String content;
    private String title;
    private Integer views;
    //tag_id
    private BlogTag tag;
    private Integer comments;
    private Integer isoriginal;

}
