package com.love.outofmemory.domain;

import lombok.Data;

@Data
public class Fan {
    private Integer id;
   private  User user;/*user_id*/
    private User fan;//fan_id

}
