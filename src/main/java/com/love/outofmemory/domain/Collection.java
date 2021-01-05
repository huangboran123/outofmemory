package com.love.outofmemory.domain;

import lombok.Data;

/**
 * @author huang
 */
@Data
public class Collection {
    private Integer id;
    /*blog_id*/
    private User user;
   /* user_id*/
    private Blog blog;
}
