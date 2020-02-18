package com.hdbsoft.spring.netty;

import com.hdbsoft.spring.netty.echo.EchoClient;
import com.hdbsoft.spring.netty.echo.EchoServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringNettyApplication {

    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("[usage] java -jar xxx.jar [echo|proxy] [server|client]");
            return;
        }

        ConfigurableApplicationContext context = SpringApplication.run(SpringNettyApplication.class, args);
        String strServer = args[0].toLowerCase();
        String strMode   = args[1].toLowerCase();
        switch(strServer) {
            case "echo":
                if(strMode.equals("server")) {
                    EchoServer echoServer = context.getBean(EchoServer.class);
                    echoServer.start();
                } else if(strMode.equals("client")) {
                    EchoClient echoClient = context.getBean(EchoClient.class);
                    echoClient.start();
                }
            case "proxy":

                break;
        }
    }
}
