package com.hdbsoft.spring.console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

@Component
public class MyCmdRunner implements CommandLineRunner  {

    @Override
    public void run(String... args) throws Exception {

//        ServerSocketChannel channel = ServerSocketChannel
//                                    .open()
//                                    .bind(InetSocketAddress.createUnresolved("127.0.0.1", 9090));
//        channel.accept();
        System.out.println("TEST!!");

    }
}
