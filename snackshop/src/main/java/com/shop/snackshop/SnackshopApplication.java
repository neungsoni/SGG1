package com.shop.snackshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
/*@EnableJpaAuditing*/ // config 관련 내용 확인 해보기
public class SnackshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(SnackshopApplication.class, args);
    }

}
