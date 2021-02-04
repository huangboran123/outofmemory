package com.love.outofmemory.controller;

import com.love.outofmemory.Utills.DateUtil;
import com.love.outofmemory.domain.Blog;
import com.love.outofmemory.domain.Comment;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private ICommentService iCommentService;


    //提交评论
    @RequestMapping(value = "/comment")
    public String comment(String comment, Integer blogId, HttpSession session){
        User user=(User) session.getAttribute("user");
        if(user!=null){
           Integer userId=user.getId();
          /* 新建评论实体*/
            Comment comment1=new Comment();
            comment1.setComment_content(comment);
            comment1.setComment_time(DateUtil.getNowDateShort());

            User critic=new User();
            critic.setId(userId);
            comment1.setCritic(critic);

            Blog blog=new Blog();
            blog.setId(blogId);
            comment1.setBlog(blog);


           int i=iCommentService.commitcomment(comment1);

        }




        return null;

    }

}
