package com.love.outofmemory.controller.front;


import com.love.outofmemory.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/front")
public class FrontRegisterController {

    @RequestMapping("/registerPage")
    public String Page(Model model){

        model.addAttribute("user",new User());
        return "front/register/register";
    }
}
