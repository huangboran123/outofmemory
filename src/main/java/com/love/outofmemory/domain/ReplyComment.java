package com.love.outofmemory.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyComment {
    private Integer id;
    //comment_id
    private Comment comment;
    private Date comment_time;
    private String comment_content;
    //from_uid
    private User from;
    //to_uid；
    private User to;



}
