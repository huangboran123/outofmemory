package com.love.outofmemory.Utills;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    public void HashPut(String mapname, String field , Object value){
       redisTemplate.opsForHash().put(mapname,field,value);

    }
    public Object HashGet(String mapname, String field ){
      return   redisTemplate.opsForHash().get(mapname,field);


    }

    public Map<Object,Object> HashGetMapEntries(String mapname){

        return  redisTemplate.opsForHash().entries(mapname);
    }






}
