package com.smm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.smm.Config.JwtConfig;
import com.smm.Config.JwtFilter;

@SpringBootApplication
public class JwtFinalApplication {

    @Autowired
    private JwtConfig jwtConfig;

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() {
        final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter(jwtConfig.secretKey()));
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(JwtFinalApplication.class, args);
        System.out.println("Project Started");
    }
}
