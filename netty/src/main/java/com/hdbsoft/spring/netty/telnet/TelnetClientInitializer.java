package com.hdbsoft.spring.netty.telnet;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

public class TelnetClientInitializer extends ChannelInitializer<SocketChannel> {

    private static final StringDecoder DECODER = new StringDecoder();
    private static final StringEncoder ENCODER = new StringEncoder();

    private static final TelnetClientHandler CLIENT_HANDLER = new TelnetClientHandler();

    private final SslContext sslCtx;

    private final String host;
    private final int port;

    public TelnetClientInitializer(SslContext sslCtx, String host, int port) {
        this.sslCtx = sslCtx;
        this.host = host;
        this.port = port;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        ChannelPipeline pipeline = sc.pipeline();
        if(sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(sc.alloc(), host, port));
        }

        pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()));
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);
        pipeline.addLast(CLIENT_HANDLER);
    }
}
