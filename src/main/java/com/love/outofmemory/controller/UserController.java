package com.love.outofmemory.controller;


import com.love.outofmemory.Utills.DateUtil;
import com.love.outofmemory.Utills.EncryptionUtils;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    private IUserService iUserService;


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
                   profilepicture.transferTo(new File("E:/JavaProject/Spring/outofmemory/src/main/resources/static/common/profile_image/"+unique_Profile_name ));
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
      session.setAttribute("user",user);

      return "登录成功";
    }

    @RequestMapping("/profilePage")
    public String profilePage(Model model){


        return "front/profile/profilePage";
    }



}