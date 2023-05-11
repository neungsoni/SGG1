package com.shop.snackshop.config;





import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadPath}") //application.properties에 있는 uploadPath 프로퍼티 값을 읽어 옴
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") // uploadPath 설정한 기준 파일을 읽어오도록 설정
                .addResourceLocations(uploadPath); // root 경로 설정
    }
}
