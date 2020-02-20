package com.hdbsoft.spring.netty;

import com.hdbsoft.spring.netty.echo.EchoClient;
import com.hdbsoft.spring.netty.echo.EchoServer;
import com.hdbsoft.spring.netty.hexproxy.HexDumpProxyBackendHandler;
import com.hdbsoft.spring.netty.hexproxy.HexProxyServer;
import com.hdbsoft.spring.netty.proxy.ProxyServer;
import com.hdbsoft.spring.netty.telnet.TelnetClient;
import com.hdbsoft.spring.netty.telnet.TelnetServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringNettyApplication {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("usage: java -jar xxx.jar [echo|proxy|hexproxy|telnet] [server|client]");
            return;
        }

        ConfigurableApplicationContext context = SpringApplication.run(SpringNettyApplication.class, args);
        String strServer = args[0].toLowerCase();
        String strMode = args[1].toLowerCase();

        Thread thread = null;
        switch (strServer) {
            case "echo":
                if (strMode.equals("server")) {
                    thread = context.getBean(EchoServer.class);
                } else if (strMode.equals("client")) {
                    thread = context.getBean(EchoClient.class);
                }
            case "proxy":
                if (strMode.equals("server")) {
                    thread = context.getBean(ProxyServer.class);
                } else if (strMode.equals("client")) {
                    //
                }
            case "hexproxy":
                if (strMode.equals("server")) {
                    thread = context.getBean(HexProxyServer.class);
                }
                break;
            case "telnet":
                if (strMode.equals("server")) {
                    thread = context.getBean(TelnetServer.class);
                } else if (strMode.equals("client")) {
                    thread = context.getBean(TelnetClient.class);
                }
                break;
        }

        if (thread == null) {
            System.out.println("NO matching server/client task!!");
            return;
        }
        thread.start();
    }
}
