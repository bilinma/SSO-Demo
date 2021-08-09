package com.jack.userservice;

import com.jack.userservice.interceptor.SessionInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class UserServiceApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 给当前应用添加拦截器，从Spring IoC容器中获取到
        // 表示拦截/user下面所有的资源
        registry.addInterceptor(sessionInterceptor()).addPathPatterns("/user/*");
    }

    @Bean   // 表示将拦截器放到IoC容器中
    public SessionInterceptor sessionInterceptor(){
        return new SessionInterceptor();
    }
}
