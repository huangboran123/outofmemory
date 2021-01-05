package com.love.outofmemory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.outofmemory.domain.Classification;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.service.IBlogService;
import com.love.outofmemory.service.IClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author huang
 */
@Controller
public class ClassificationController {


    @Autowired
    private IClassificationService iClassificationService;
    @Autowired
    private IBlogService iBlogService;



    @PostMapping(value = "/myblog/newClass",produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String newClass(String classifyName, HttpSession session){

        User user=(User)session.getAttribute("user");
        int i=iClassificationService.addClassification(user.getId(),classifyName);

        if(i==0){
            return "创建分类失败";
        }
        else {
            return "创建分类成功";
        }

    }
    @PostMapping(value = "/myblog/classjson",produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String getClass(HttpSession session){
        User user=(User)session.getAttribute("user");
        /*新建json工具对象*/
        ObjectMapper classjosn=new ObjectMapper();
       List<Classification> classList=iClassificationService.getDiyClassificationByUserID(user.getId());
        try {
            return classjosn.writeValueAsString(classList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }

    }
    @PostMapping(value = "/myblog/deleteclass",produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    //用springmvc @RequestBody注解做提交json字符串自动绑定到pojo入参时，
    // 类型需要是"application/json;charset=UTF-8"，否则会抛"not supported"异常。
    public String getClass(HttpSession session,@RequestBody Integer[] classDeleteId ){

        User user=(User)session.getAttribute("user");
        Integer uid=user.getId();
        //移入默认
       int i=iBlogService.moveBlogtoDefaultclass(uid,classDeleteId);

       int j=iClassificationService.deleteClassByuidAndclassid(uid,classDeleteId);
       if(j!=classDeleteId.length){
           return "删除分类失败";
       }

       return "删除成功";
    }

    /*produces为返回数据类型*/
    @PostMapping(value = "/myblog/movblog",produces = {"text/plain;charset=UTF-8"})
    @ResponseBody

    public String movBlog(HttpSession session,Integer blogClassid){
        User user=(User)session.getAttribute("user");
        Integer uid=user.getId();
        ObjectMapper classListJson=new ObjectMapper();
        List<Classification> classificationList=iClassificationService.getMovclass(uid,blogClassid);
       if(classificationList.size()==0){
           return "请您先创建分类";
       }

        try {
            return classListJson.writeValueAsString(classificationList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

       return "服务器错误";
    }

    @PostMapping(value = "/myblog/movblog.do",produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String movBlogSubmit(Integer blogId,Integer movchoice){

        if(movchoice==null){
            return "请选中移入分类";
        }

       int i=iBlogService.moveBlogtootherclass(blogId,movchoice);
       if(i==1){
           return "移动成功";
       }
       else{
           return "服务器错误";
       }

    }

    }
