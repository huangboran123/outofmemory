package com.love.outofmemory.controller.front;

import com.love.outofmemory.domain.Blog;
import com.love.outofmemory.domain.BlogTag;
import com.love.outofmemory.domain.User;
import com.love.outofmemory.service.IBlogService;
import com.love.outofmemory.service.IBlogTagService;
import com.love.outofmemory.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller

public class FrontIndexController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IBlogService iBlogService;
    @Autowired
    private IBlogTagService iBlogTagService;

    /*首页*/
    @RequestMapping("/")
    public String indexPage(Model model, HttpSession session, HttpServletRequest request){

        User user = new User();
        //首页判断浏览器是否还存在用户数据
        //这里可以将user用户信息存入redis缓存，而不使用服务器session
        //用户退出时，删除redis相应key即可

        Integer page=1;
        Integer pagesize=5;

        List<BlogTag> blogTagList=iBlogTagService.getBlogTags();
        model.addAttribute("blogTagList",blogTagList);



        /*首页推荐博客*/
        List<Blog> blogList=iBlogService.getIndexRecommandblogs(page,pagesize);
        model.addAttribute("blogList",blogList);


        /*根据博客类别来查询博客数*/
        Integer totalcount=iBlogService.getTotalcountbyclass(0);
        model.addAttribute("tottalcount",totalcount%pagesize>0 ? (totalcount/pagesize)+1:totalcount/pagesize);


        if (session.getAttribute("user") != null) {
            return "front/index";

        } else {

            //获取COOKIE
            Cookie[] cookies = request.getCookies();
            //判断是否处于生命期
            int flag = 0;
            //若请求头中存在cookie
            if (cookies != null && cookies.length > 0) {
                for (Cookie c : cookies) {
                    //判断对应cookie是否还在生存期
                    if (c.getName().equals("loginname")) {
                       user.setPhone(c.getValue());
                    }
                    if (c.getName().equals("password")) {
                        user.setPassword(c.getValue());
                    }
                }

            }

            //若存在则重查数据库
            //浏览器端存在COOKEI则重新设置SESSION
            if (user.getPhone() != null && user.getPassword() != null) {

              User user2= iUserService.getUserByloginname(user.getPhone());
                session.setAttribute("user", user2);

            }
        }


        return "front/index";


    }
}
