package com.hdbsoft.spring.netty.echo;

import com.hdbsoft.spring.netty.config.ConfigData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EchoServer extends Thread {
    private Logger logger = LoggerFactory.getLogger(EchoServer.class);

    @Value("${tcp.port}")
    private int port;

    @Value("${boss.thread.count}")
    private int bossCount;

    @Value("${worker.thread.count}")
    private int workerCount;

    @Override
    public void run() {
        logger.info("Netty server is starting...");
        logger.info(String.format("{tcp.port=%s, boss.thread.count=%s, worker.thread.count=%s}", port, bossCount, workerCount));

        //클라이언트 연결 수락 쓰레드 그룹
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossCount);

        //연결된 클라이언트 소켓으로부터 데이터 입출력 및 이벤트를 담당하는 쓰레드 그룹
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerCount);

        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)                  //서버소켓 입출력 모드를 NIO로 설정
              .handler(new LoggingHandler())                          //서버소켓 채널 핸들러 등록
              .childHandler(new ChannelInitializer<SocketChannel>() { //송수신 데이터 가공 핸들러
                  @Override
                  protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                        pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,
                                //twice newline
                                new ByteBuf[]{Unpooled.wrappedBuffer(ConfigData.DELIMETERS[0].getBytes()),
                                              Unpooled.wrappedBuffer(ConfigData.DELIMETERS[1].getBytes())}));
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new EchoServerHandler());
                  }
              });

            ChannelFuture cf = sb.bind(port).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(e.toString());
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
