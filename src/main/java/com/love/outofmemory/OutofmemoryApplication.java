package com.love.outofmemory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author huang
 */
@SpringBootApplication
@MapperScan("com.love.outofmemory.mapper")
@EnableScheduling
public class OutofmemoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutofmemoryApplication.class, args);
    }
}
