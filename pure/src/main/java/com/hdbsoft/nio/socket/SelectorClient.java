package com.hdbsoft.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class SelectorClient {

    public static void main(String[] args) {

        final int PORT = 5555;
        final String IP = "127.0.0.1";

        ByteBuffer buffer = ByteBuffer.allocateDirect(2 * 1024);
        ByteBuffer randomBuffer;
        CharBuffer charBuffer;

        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();

        try(Selector selector = Selector.open();
            SocketChannel channel = SocketChannel.open()) {

            if((selector.isOpen()) && (channel.isOpen())) {
                channel.configureBlocking(false);

                channel.setOption(StandardSocketOptions.SO_RCVBUF, 128 * 1024);
                channel.setOption(StandardSocketOptions.SO_SNDBUF, 128 * 1024);
                channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);

                channel.register(selector, SelectionKey.OP_CONNECT);
                channel.connect(new InetSocketAddress(IP, PORT));

                while(selector.select(1000) > 0) {
                    Set keys = selector.selectedKeys();
                    Iterator its = keys.iterator();

                    while(its.hasNext()) {
                        SelectionKey key = (SelectionKey)its.next();
                        its.remove(); //현재키를 제거한다.

                        try(SocketChannel keyChannel = (SocketChannel)key.channel()) {
                            if(key.isConnectable()) {
                                System.out.println("connected!!");

                                if(keyChannel.isConnectionPending()) {
                                    keyChannel.finishConnect();
                                }

                                while(keyChannel.read(buffer) != -1) {
                                    buffer.flip();

                                    charBuffer = decoder.decode(buffer);
                                    System.out.println(charBuffer.toString());

                                    if(buffer.hasRemaining()) {
                                        buffer.compact();
                                    } else {
                                        buffer.clear();
                                    }

                                    int rand = new Random().nextInt(100);
                                    if(rand == 50) {
                                        System.out.println("50 was generated!! close the socket channel");
                                        break;
                                    } else {
                                        randomBuffer = ByteBuffer.wrap("random number => ".concat(String.valueOf(rand)).getBytes());
                                        keyChannel.write(randomBuffer);
                                    }
                                    Thread.sleep(500);
                                }
                            }
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
