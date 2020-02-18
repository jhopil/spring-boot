package com.hdbsoft.spring.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EchoClient extends Thread {
    private Logger logger = LoggerFactory.getLogger(EchoClient.class);

    @Value("${tcp.host}")
    private String host;

    @Value("${tcp.port}")
    private int port;

    @Override
    public void run() {
        logger.info("Netty client is starting...");

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap boot = new Bootstrap();
            boot.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline cp = sc.pipeline();
                        cp.addLast(new EchoClientHandler());
                    }
                });

            ChannelFuture cf = boot.connect(host, port).sync();
            cf.channel().closeFuture().sync();

        } catch(Exception e) {
            logger.warn(e.toString());
        } finally {
            group.shutdownGracefully();
        }
    }
}
