package com.love.outofmemory.domain;


import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Integer id;
    private User user;//user_id
    private User sendto;//sendto_id
    private Date send_time;
    private String context;

}
