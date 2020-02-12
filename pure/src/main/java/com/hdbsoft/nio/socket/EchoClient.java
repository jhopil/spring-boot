package com.hdbsoft.nio.socket;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Random;

public class EchoClient {

    public static void main(String[] args) {

        final int PORT = 5555;
        final String IP = "127.0.0.1";

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        ByteBuffer helloBuffer = ByteBuffer.wrap("Hello !".getBytes());
        ByteBuffer randomBuffer;
        CharBuffer charBuffer;

        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();

        try(SocketChannel channel = SocketChannel.open()) {

            if(channel.isOpen()) {
                channel.configureBlocking(true);

                channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                channel.setOption(StandardSocketOptions.SO_LINGER, 5);

                channel.connect(new InetSocketAddress(IP, PORT));
                if(channel.isConnected()) {
                    channel.write(helloBuffer);

                    int count = 0;
                    while(channel.read(buffer) != 1) {
                        buffer.flip();

                        charBuffer = decoder.decode(buffer);
                        System.out.println(charBuffer.toString());

                        if(buffer.hasRemaining()) {
                            buffer.compact();
                        } else {
                            buffer.clear();
                        }

                        int rand = new Random().nextInt(10000);
                        if(rand == 50) {
                            System.out.println("50 was generated!! close the socket channel");
                            break;
                        } else {
                            randomBuffer = ByteBuffer.wrap("random number => ".concat(String.valueOf(rand)).concat(" => count: " + count++).getBytes());
                            channel.write(randomBuffer);
                        }
                        //Thread.sleep(500);
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
