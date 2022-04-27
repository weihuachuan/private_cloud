package com.huachuan.bigdata.hos.web;

import com.huachuan.bigdata.hos.mybatis.HosDataSourceConfig;
import com.huachuan.bigdata.hos.web.security.SecurityInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@SuppressWarnings("deprecation")
@EnableAutoConfiguration(exclude = MongoAutoConfiguration.class)
@Configuration
@ComponentScan({"com.huachuan.bigdata.*"})
@SpringBootApplication
@Import({HosDataSourceConfig.class, HosServerBeanConfiguration.class})
@MapperScan("com.huachuan.bigdata")
public class HosServerApp {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private SecurityInterceptor securityInterceptor;

    public static void main(String[] args) {
        SpringApplication.run(HosServerApp.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }

            @Override
            public void addInterceptors(InterceptorRegistry registration) {
                registration.addInterceptor(securityInterceptor);
            }
        };
    }

}
