package com.hdbsoft.spring.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SpringNettyApplication {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("[usage] java -jar xxx.jar [server|client]");
            return;
        }

        ConfigurableApplicationContext context = SpringApplication.run(SpringNettyApplication.class, args);
        switch(args[0]) {
        case "server":
            NettyServer nettyServer = context.getBean(NettyServer.class);
            nettyServer.start();
            break;
        case "client":
            NettyClient nettyClient = context.getBean(NettyClient.class);
            nettyClient.start();
            break;
        }
    }
}
