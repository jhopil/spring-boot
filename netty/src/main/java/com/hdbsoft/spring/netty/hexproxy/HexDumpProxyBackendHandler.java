package com.hdbsoft.spring.netty.hexproxy;

import io.netty.channel.*;

public class HexDumpProxyBackendHandler extends ChannelInboundHandlerAdapter {

    private final Channel inboundChannel;

    public HexDumpProxyBackendHandler(Channel inboundChannel) {
        this.inboundChannel = inboundChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        HexDumpProxyFrontHandler.closeOnFlush(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        inboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()) {
                    ctx.channel().read();
                } else {
                    future.channel().close();
                }
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        HexDumpProxyFrontHandler.closeOnFlush(ctx.channel());
    }
}
