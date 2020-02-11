package com.hdbsoft.spring.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    public static final ServiceHandler INSTANCE = new ServiceHandler();

    private Logger logger = LoggerFactory.getLogger(ServiceHandler.class);

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private ServiceHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channels.add(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String byteBuf = (String)msg;
        logger.debug("{} => message: {} ", this, byteBuf);

        ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(((String) msg).getBytes()));

    }
}
