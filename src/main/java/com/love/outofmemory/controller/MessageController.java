package com.love.outofmemory.controller;

import com.love.outofmemory.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class MessageController {

    @RequestMapping(value = "/chatPage/{followId}",method = RequestMethod.GET)
    public  String chatPage(HttpSession session, Model model, @PathVariable("followId") Integer followId){
        User user=(User)session.getAttribute("user");
        if(user==null){
            return "front/login/login";
        }else{
            return "front/message/messagePage";
        }

    }


}
