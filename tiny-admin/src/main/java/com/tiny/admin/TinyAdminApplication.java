package com.tiny.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 主启动类
 */
@Slf4j
@EnableAsync
@SpringBootApplication(scanBasePackages = "com.tiny")
@MapperScan("com.tiny.**.mapper")
public class TinyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TinyAdminApplication.class, args);
    }
}
