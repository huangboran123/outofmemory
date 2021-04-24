package com.love.outofmemory.configuration;

import com.love.outofmemory.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class VisitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user =(User)request.getSession().getAttribute("user");

        if(Objects.isNull(user)){

          redirect(request,response);
            return false;
        }
        else {
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


    public void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取当前请求的路径
        //logger.error("请求类型:"+request.getHeader("X-Requested-With"));
        response.setHeader("Access-Control-Expose-Headers", "REDIRECT,CONTEXTPATH");
        //告诉ajax我是重定向
        response.setHeader("REDIRECT", "REDIRECT");
        //告诉ajax我重定向的路径
        String registerurl="/front/registerPage";
        String loginurl="/front/loginPage";
        response.setHeader("CONTEXTPATH", loginurl);
        response.getWriter().write(1);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }


}
