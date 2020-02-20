package com.hdbsoft.spring.netty.echo;

import com.hdbsoft.spring.netty.util.StringUtils;
import io.netty.buffer.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(EchoClientHandler.class);

    private ByteBuf getCurrentDate() {
        return Unpooled.wrappedBuffer(new Date().toString().concat(StringUtils.DELIMITERS[0]).getBytes());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //처음 연결되었을때 데이터를 한번 보낸다.
        ctx.writeAndFlush(getCurrentDate());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf)msg;
        //읽을 수 있는 바이트의 길이를 가져온다.
        int size = buffer.readableBytes();

        String strMsg = buffer.toString(0, size, Charset.defaultCharset());
        logger.info("read data => " + strMsg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        try { TimeUnit.SECONDS.sleep(1); } catch(InterruptedException e) {}

        ctx.writeAndFlush(getCurrentDate());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(cause.toString());
        ctx.close();
    }
}
