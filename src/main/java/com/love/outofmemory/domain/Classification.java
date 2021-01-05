package com.love.outofmemory.domain;

import lombok.Data;

/**
 * @author huang
 */
@Data
public class Classification {
     private Integer id;
     //user_id
     private User user;

     private String name;

}
