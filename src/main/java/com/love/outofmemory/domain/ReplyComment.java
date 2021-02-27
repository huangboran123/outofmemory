package com.love.outofmemory.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyComment {
    private Integer id;
    //comment_id
    private Comment comment;
    private Date reply_time;
    private String reply_content;
    //from_uid
    private User from;
    //to_uid；
    private User to;



}
