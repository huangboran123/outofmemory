package com.love.outofmemory.configuration;

import com.love.outofmemory.Utills.RedisUtil;
import com.love.outofmemory.service.IBlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * description: TimeTask
 * date: 2021/4/27 下午10:26
 * author: huangboran
 * version: 1.0
 */
@Component
@Slf4j
public class TimeTask {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IBlogService iBlogService;



    @Scheduled(cron = "0 10,44 14 ? 3 WED")
    public void syncPostViews(){
        log.info("======================开始 同步文章访问量======================");
        Long startTime = System.nanoTime();
        Map<Object,Object> viewmap=redisUtil.HashGetMapEntries("Views");

        int i=iBlogService.saveredisViews(viewmap);




   }







}
