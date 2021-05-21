package com.love.outofmemory.controller;

import com.love.outofmemory.Utills.DateUtil;
import com.love.outofmemory.annotation.LogInterceptor;
import com.love.outofmemory.domain.Blog;
import com.love.outofmemory.domain.Comment;
import com.love.outofmemory.domain.ReplyComment;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.service.ICommentService;
import com.love.outofmemory.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private ICommentService iCommentService;
 /*   @Autowired
    private IUserService iUserService;*/



    //提交评论
    @PostMapping(value = "/comment",produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    @LogInterceptor
    public String comment(String comment, Integer blogId, HttpSession session){
        User user=(User) session.getAttribute("user");
           Integer userId=user.getId();
          /* 新建评论实体*/
            Comment comment1=new Comment();
            comment1.setComment_content(comment.trim());
            comment1.setComment_time(DateUtil.getNowDateLong());
            User critic=new User();
            critic.setId(userId);
            comment1.setCritic(critic);
            Blog blog=new Blog();
            blog.setId(blogId);
            comment1.setBlog(blog);
           int i=iCommentService.commitcomment(comment1);
           int j=iCommentService.commentaddonebyid(blogId);
           if(i==1){
               return "评论提交成功";
           }
           else{
               return "评论提交失败";
           }
    }
  /*  提交回复*/
    @PostMapping(value = "/reply",produces ={"text/plain;charset=UTF-8"} )
    @ResponseBody
    @LogInterceptor
    public String reply(Integer commentId,Integer toUId,String replyContent,Integer blogId,HttpSession session){
        User user=(User) session.getAttribute("user");
            /*新建回复实例对象*/
            ReplyComment replyComment=new ReplyComment();
            Comment comment=new Comment();
            comment.setId(commentId);
            replyComment.setComment(comment);
            replyComment.setReply_content(replyContent);
            User touser=new User();
            touser.setId(toUId);
            replyComment.setTo(touser);
            replyComment.setReply_time(DateUtil.getNowDateLong());
            replyComment.setFrom(user);
            int i=iCommentService.commitreply(replyComment);
            int j=iCommentService.commentaddonebyid(blogId);
            if(i==1){
                return "回复成功";
            }
            else{
                return "回复失败";
            }
    }
}
