package com.hdbsoft.spring.netty.telnet;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class TelnetClient extends Thread {

    @Value("${telnet.ssl}")
    private boolean ssl;

    @Value("${telnet.host}")
    private String host;

    @Value("${telnet.port}")
    private int port;

    @Override
    public void run() {
        SslContext sslCtx = null;
        try {
            if (ssl) {
                sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            }
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(clientGroup)
              .channel(NioSocketChannel.class)
              .handler(new TelnetClientInitializer(sslCtx, host, port));

            Channel ch = bs.connect(host, port).sync().channel();
            ChannelFuture future = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for(;;) {
                String line = in.readLine();
                if(line == null) { break; }

                future = ch.writeAndFlush(line + "\r\n");
                if("bye".equals(line.toLowerCase())) {
                    ch.closeFuture().sync();
                    break;
                }
            }

            if(future != null) {
                future.sync();
            }

        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            clientGroup.shutdownGracefully();
        }

    }
}
