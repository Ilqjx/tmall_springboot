package com.ilqjx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CORSConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        // 跨域资源共享，所有请求都允许跨域
        registry.addMapping("/**") // 配置可以被跨域的路径
                .allowedOrigins("*")  // 允许所有的请求域名访问跨域资源
                .allowedMethods("*")  // 允许跨域访问资源服务器的请求方式(GET、POST...)
                .allowedHeaders("*"); // 允许所有的请求头访问
        super.addCorsMappings(registry);
    }

    @Override
    // 配置静态资源
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // addResourceHandler() 为url的请求路径
        // addResourceLocations() 为服务器真实路径
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    }

}
