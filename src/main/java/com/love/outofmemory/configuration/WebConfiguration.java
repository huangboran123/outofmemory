package com.love.outofmemory.configuration;

import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * web配置
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

   /* @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 解决controller返回字符串中文乱码问题
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
            } else if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }*/

    //解决Java校验读取配置文件中文乱码
    @Override
    protected Validator getValidator() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("utf-8");// 读取配置文件的编码格式
        messageSource.setCacheMillis(-1);// 缓存时间，-1表示不过期
        messageSource.setBasename("ValidationMessages");// 配置文件前缀名，设置为Messages,那你的配置文件必须以Messages.properties/Message_en.properties...

        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory();
        factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        factoryBean.setValidationMessageSource(messageSource);
        return factoryBean;
    }

    //解决静态资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        //addResourceHandler请求路径
        //addResourceLocations 在项目中的资源路径
        //映射顶级路径静态资源
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);

        //获取文件的真实路径 work_project代表项目工程名 需要更改
        //配置虚拟路径映射访问，因为对服务器的保护措施导致的，服务器不能对外部暴露真实的资源路径。
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            /*图片资源请求路径映射*/
            registry.addResourceHandler("/common/blog_image/**").
                    addResourceLocations("classpath:/static/common/blog_image/");
            registry.addResourceHandler("/common/image_cache/**").
                    addResourceLocations("classpath:/static/common/image_cache/");
            /* 某些请求路径下，在请求样式表等资源时会携带请求路径名，导致访问不到资源，我们可以加上路径名如/front*/
            /*粒子效果资源请求路径映射*/
            registry.addResourceHandler("/front/particles/**","/myblog/particles/**","/particles/**").
                    addResourceLocations("classpath:/static/particles/");
            registry.addResourceHandler("/editor/**","/myblog/editor/**").
                    addResourceLocations("classpath:/static/editor/");
            //jquery映射
            registry.addResourceHandler("/myblog/js/**","/js/**").
                    addResourceLocations("classpath:/static/js/");
            registry.addResourceHandler("/css/**").
                    addResourceLocations("classpath:/static/css/");
            registry.addResourceHandler("/common/**").
                    addResourceLocations("classpath:/static/common/");
            registry.addResourceHandler("/blog/**","/").
                    addResourceLocations("classpath:/static/");
        } else {//linux和mac系统 可以根据逻辑再做处理
            /*图片资源请求路径映射*/
            registry.addResourceHandler("/common/blog_image/**").
                    addResourceLocations("classpath:/static/common/blog_image/");
            registry.addResourceHandler("/common/image_cache/**").
                    addResourceLocations("classpath:/static/common/image_cache/");
            /* 某些请求路径下，在请求样式表等资源时会携带请求路径名，导致访问不到资源，我们可以加上路径名如/front*/
            /*粒子效果资源请求路径映射*/
            registry.addResourceHandler("/front/particles/**","/myblog/particles/**","/particles/**").
                    addResourceLocations("classpath:/static/particles/");
            registry.addResourceHandler("/editor/**","/myblog/editor/**").
                    addResourceLocations("classpath:/static/editor/");
            //jquery映射
            registry.addResourceHandler("/myblog/js/**","/js/**").
                    addResourceLocations("classpath:/static/js/");
            registry.addResourceHandler("/css/**").
                    addResourceLocations("classpath:/static/css/");
           registry.addResourceHandler("/common/**").
                  addResourceLocations("classpath:/static/common/");
            registry.addResourceHandler("/blog/**","/").
                    addResourceLocations("classpath:/static/");
        }
        super.addResourceHandlers(registry);
    }

/*配置拦截器*/
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        //这里使用注解进行登录拦截，在这里配置过于麻烦
        registry.addInterceptor(new VisitInterceptor())
                .excludePathPatterns("/common/blog_image/**")
                .excludePathPatterns("/common/image_cache/**")
                .excludePathPatterns("/front/particles/**","/myblog/particles/**","/particles/**")
                .excludePathPatterns("/editor/**","/myblog/editor/**")
                .excludePathPatterns("/myblog/js/**","/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/common/**")
                .excludePathPatterns("/blog/**","/");
    }
}





