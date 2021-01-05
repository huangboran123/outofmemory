package com.love.outofmemory.domain;


import lombok.Data;

@Data
public class Follow {
    private Integer id;
    private User user;//user_id
    private User follow;//follow_id
}
