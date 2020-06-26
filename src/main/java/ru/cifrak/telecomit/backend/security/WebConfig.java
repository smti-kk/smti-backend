package ru.cifrak.telecomit.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//@EnableWebMvc
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/admin/index").setViewName("/admin/index");
//        registry.addViewController("/admin/loginPage").setViewName("/admin/loginPage");
//    }
//
//    /**
//     * This for configuring serve static files <br/>
//     * https://stackoverflow.com/a/33852040/1679702 <br/>
//     * https://www.baeldung.com/spring-mvc-static-resources#3-configuring-multiple-locations-for-a-resource
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry
//                .addResourceHandler("/media/**")
//                .addResourceLocations("classpath:/static/");
//    }
//}
