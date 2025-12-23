package com.tiny.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 主启动类
 */
@Slf4j
@EnableAsync
@SpringBootApplication(scanBasePackages = "com.tiny")
@MapperScan("com.tiny.**.mapper")
public class TinyAdminApplication {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = SpringApplication.run(TinyAdminApplication.class, args);
        Environment env = context.getEnvironment();

        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");

        log.info("\n----------------------------------------------------------\n" +
                "  Tiny Admin 启动成功!\n" +
                "----------------------------------------------------------\n" +
                "  后端API地址:\n" +
                "    Local:    http://localhost:{}{}\n" +
                "    Network:  http://{}:{}{}\n" +
                "  接口文档地址:\n" +
                "    Knife4j:  http://localhost:{}{}/doc.html\n" +
                "----------------------------------------------------------",
                port, contextPath, ip, port, contextPath, port, contextPath);
    }
}
