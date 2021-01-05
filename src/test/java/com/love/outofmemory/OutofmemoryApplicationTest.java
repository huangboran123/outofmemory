package com.love.outofmemory;


import com.love.outofmemory.Utills.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@SpringBootTest
public class OutofmemoryApplicationTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {
     /*  redisUtil.StringSet("fav","we love java");

        System.out.println( redisUtil.StringGet("fav"));*/



    }
}
