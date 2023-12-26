package com.getcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GetcodeApplication {
    public static void main(String[] args) {

        SpringApplication.run(GetcodeApplication.class, args);
    }

}
