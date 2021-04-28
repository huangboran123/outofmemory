package com.love.outofmemory.configuration;

import com.love.outofmemory.annotation.LogInterceptor;
import com.love.outofmemory.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 拦截器配置
 */
@Component
public class VisitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取方法处理器
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LogInterceptor logInterceptor =
                handlerMethod.getMethod()//这一步是获取到我们要访问的方法
                        //然后根据我们制定的自定义注解的Class对象来获取到对应的注解
                        .getAnnotation(LogInterceptor.class);

        //如果要访问的方法上没有加这个注解，那么就说明这个方法不需要拦截，否则就需要进行拦截
        if(null == logInterceptor) {
            return true;
        }
        else{
            User user =(User)request.getSession().getAttribute("user");
            if(Objects.isNull(user)){
                redirect(request,response);
                return false;
            }
            else {
                return true;
            }
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
