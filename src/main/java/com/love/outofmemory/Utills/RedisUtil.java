package com.love.outofmemory.Utills;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author huang
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;


    public void StringSet(String key,Object value){

     redisTemplate.opsForValue().set(key,value);

    }

    public Object StringGet(String key){

       return redisTemplate.opsForValue().get(key);

    }





}
