package com.love.outofmemory.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.love.outofmemory.Utills.CodeUtil;
import com.love.outofmemory.Utills.DateUtil;
import com.love.outofmemory.Utills.EncryptionUtils;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.domain.view.ProfilePageUser;
import com.love.outofmemory.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * @author huang
 */
@Controller
public class UserController {

    private String imgPathP = "/home/huang/outofmemory/resources/static/common/profile_image/";

    @Autowired
    private IUserService iUserService;


   /* 注册*/
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(
            @RequestPart("profilepicture") MultipartFile profilepicture,
            @Valid User user,  //验证字段合法性
            Errors errors,
            String confirmpassword,
            String str_birthday,
            Model model,
            HttpSession session,
            String code
           ){

        //启用Java校验的表单字段是否符合规范
       if(errors.hasErrors()){

           return "/front/register/register";
       }

       //生日是否为空串
       if("".equals(str_birthday)){

           model.addAttribute("birthday","请输入生日");
           return "/front/register/register";
        }

       //密码是否一致
       if(!user.getPassword().equals(confirmpassword)){

            model.addAttribute("password_dismatch","密码输入不一致");
            return "front/register/register";
        }

        //判断图片是否为空，这里可以使用默认图像
       /* if(profilepicture.isEmpty()){

            return "front/register/register";
        }*/

        //验证码
       if(!session.getAttribute("code").equals(code)){
           model.addAttribute("code_dismatch","验证码输入错误");
            return "front/register/register";

       }
       else{

           String unique_Profile_name="default_profile.png";
           //密码MD5加密
           String password_md5= EncryptionUtils.encryptMD5(user.getPassword());
           //图片不为空则将图片转移到相应路径
           if(!profilepicture.isEmpty()){

               try {

                   //获取图像后缀名
                   String[] houzui= Objects.requireNonNull(profilepicture.getOriginalFilename()).split("[.]");
                   //生成图像唯一名
                   unique_Profile_name=UUID.randomUUID().toString().replace("-", "")+"."+houzui[1];
                   //转移图片位置
                   profilepicture.transferTo(new File(imgPathP+unique_Profile_name ));
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

            /* model.addFlashAttribute("spitter",spitter);*/
            user.setImage(unique_Profile_name);
            user.setPassword(password_md5);
            user.setBirthday(DateUtil.strToDateShort(str_birthday));
            user.setCreate_time(DateUtil.getNowDateShort());
           //默认空字符串
            user.setReputation("");
            user.setFans(0);
            user.setFollow(0);

            int i= iUserService.newUser(user);

            if(i==0){
                model.addAttribute("service_error","服务器故障");
                return "front/register/register";
            }

            else{
                //跳转到登录界面
                model.addAttribute("register_info","注册成功!");
                return "front/login/login";

            }

       }



    }

    /*登录*/
    //produces解决返回中文乱码
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String login(String loginname, String password, String code,
                        String autologin, HttpSession session, HttpServletResponse response){

        if(loginname.equals("")){
            return "用户名错误";
        }

      User user= iUserService.getUserByloginname(loginname);

      if(user==null){

          return "用户名错误";
      }
      if(!user.getPassword().equalsIgnoreCase(EncryptionUtils.encryptMD5(password))){

          return "密码错误";
      }

      if(!session.getAttribute("code").equals(code)){
          return "验证码输入有误";
      }

      if("1".equals(autologin)){
          //cookie的路径设置尤为重要，主要看你是在哪个请求资源的路径下调用request.getCookies()方法，否则无法获取到
          //如cookie1的路径为/front/login 而你想要获取cookie1就必须在/front/login的请求中获取。
          //age表示时长30天
          Cookie login_name = new Cookie("loginname", loginname);
          login_name.setMaxAge(60 * 60 * 24 * 30);
          login_name.setPath("/");
          response.addCookie(login_name);

          Cookie pass_word = new Cookie("password", password);
          pass_word.setMaxAge(60 * 60 * 24 * 30);
          pass_word.setPath("/");
          response.addCookie(pass_word);

          Cookie user_name = new Cookie("username", user.getUsername());
          user_name.setMaxAge(60 * 60 * 24 * 30);
          user_name.setPath("/");
          response.addCookie(user_name);
      }
      //服务的写入session
      session.setAttribute("user",user);

      return "登录成功";
    }

    /*个人资料修改页面*/
    @RequestMapping("/profilePage")
    public String profilePage(Model model,HttpSession session,Integer userId){
        User user=(User)session.getAttribute("user");
        if(user!=null){

            return "front/profile/profilepage";
        }
        else{
            return "redirect:/";
        }


    }

    /*查询个人资料*/
    @PostMapping(value = "/profilePage/getuserInfo",produces ={"text/plain;charset=UTF-8"})
    @ResponseBody
    public String getUserView(Integer userId){
      ProfilePageUser userInfo=iUserService.getProfileUserById(userId);

        ObjectMapper objectMapper=new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(userInfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "查询个人信息失败";
        }

    }
   /* 修改个人资料*/
    @PostMapping(value = "/profilePage/profile",produces ={"application/json;charset=UTF-8"})
    @ResponseBody
   /* 前端JS要传送一个JS对象，springMVC中使用@RequestBody来解析JSON字符串转化为对应的JAVA对象*/
    public Boolean modifyprofile(@RequestBody User muser,HttpSession session){

        User user=(User)session.getAttribute("user");
        if(user!=null){
            Integer uid=user.getId();
            muser.setId(uid);
            int i=iUserService.updateUserById(muser);
            return i == 1;

        }
        else{
            return false;
        }


    }

   /* 查询个人密码是否一致*/
   @PostMapping(value = "/passwdtest",produces ={"application/json;charset=UTF-8"})
   @ResponseBody
   public Boolean passwdissame(HttpSession session,String originalpasswd){

       User user=(User)session.getAttribute("user");
       if(user!=null){
           return user.getPassword().equals(EncryptionUtils.encryptMD5(originalpasswd));
       }
       else{
           return false;
       }
   }

    /* 修改密码*/
    @PostMapping(value = "/passwdchange",produces ={"application/json;charset=UTF-8"})
    @ResponseBody
    public Boolean passwdchange(HttpSession session,String newpasswd){

        User user=(User)session.getAttribute("user");
        if(user!=null){
           Integer userId=user.getId();
           if(userId!=null&&newpasswd!=null ){
              String  newpasswdMd5=EncryptionUtils.encryptMD5(newpasswd);
             int i=iUserService.updateUserpasswdById(userId,newpasswdMd5);
              return i == 1;

           }
           else return false;
        }
        else{
            return false;
        }
    }

}
