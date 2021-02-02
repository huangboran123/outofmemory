package com.love.outofmemory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CommentController {

    @RequestMapping(value = "/comment")
    public String comment(String comment,Integer userId){


        return null;
    }

}
