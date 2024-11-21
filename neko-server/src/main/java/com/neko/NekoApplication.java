package com.neko;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.neko.mapper")
@EnableTransactionManagement
public class NekoApplication {
    public static void main(String[] args) {
        SpringApplication.run(NekoApplication.class, args);
    }
} 