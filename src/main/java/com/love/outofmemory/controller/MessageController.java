package com.love.outofmemory.controller;

import com.love.outofmemory.domain.Message;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.service.IMessageService;
import com.love.outofmemory.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IMessageService iMessageService;

    @RequestMapping(value = "/chatPage/{followId}",method = RequestMethod.GET)
    public  String chatPage(HttpSession session, Model model, @PathVariable("followId") Integer followId){
        User user=(User)session.getAttribute("user");
        if(user!=null){

            /*当前交流对象*/
            User CurrentContact=iUserService.getUserById(followId);
            model.addAttribute("CurrentContact",CurrentContact);
            /*查询最近聊天对象*/
            List<User> RecentContact=iMessageService.getRecentContactByUserId(1,5,user.getId());
            model.addAttribute("RecentContact",RecentContact);
            /*获取当前聊天对象历史记录*/
            List<Message> CurrentContactMessage=iMessageService.getMessageContactByUserIdAndFollowId(1,4,user.getId(),followId);

            //判断用户是否在线
            if(WebSocketServer.webSocketMap.containsKey(followId.toString())){
                model.addAttribute("isonline",true);
            }
            else {
                model.addAttribute("isonline",false);
            }

            return "front/message/messagePage";
        }else{
            return "front/login/login";
        }

    }


}
