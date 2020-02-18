package com.hdbsoft.spring.netty;

import com.hdbsoft.spring.netty.echo.EchoClient;
import com.hdbsoft.spring.netty.echo.EchoServer;
import com.hdbsoft.spring.netty.proxy.ProxyServer;
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
                if(strMode.equals("server")) {
                    ProxyServer proxyServer = context.getBean(ProxyServer.class);
                    proxyServer.start();
                } else if(strMode.equals("client")) {
                    //
                }
                break;
        }
    }
}
