package com.love.outofmemory.controller.front;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/front")
public class FrontLoginController {


    @RequestMapping("/loginPage")
    public String Page(Model model){

        return "front/login/login";
    }
    @RequestMapping(value = "/loginout",method = RequestMethod.GET)
    public String LoginOut(HttpSession session, HttpServletRequest request, HttpServletResponse response){

        //清空特定cookie,只有复写cookie设置age为0,复写时注意cookie路径
        Cookie[] cookeis=request.getCookies();
        for (Cookie c:cookeis) {
            if(c.getName().equals("loginname")){
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
            }
            if(c.getName().equals("password")){
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
            }
            if(c.getName().equals("username")){
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }

        session.removeAttribute("user");


        return "redirect:/";
    }
}
