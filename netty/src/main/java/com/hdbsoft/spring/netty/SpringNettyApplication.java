package com.hdbsoft.spring.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringNettyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringNettyApplication.class, args);
        NettyServer nettyServer = context.getBean(NettyServer.class);
        nettyServer.start();
    }
}
