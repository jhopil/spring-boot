package com.hdbsoft.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.Future;

public class AsyncClient {

    public static void future() {
        final int PORT = 5557;
        final String IP = "127.0.0.1";

        ByteBuffer helloBuffer = ByteBuffer.wrap("hello".getBytes());
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        ByteBuffer randomBuffer;

        try(AsynchronousSocketChannel channel = AsynchronousSocketChannel.open()) {
            if(channel.isOpen()) {
                channel.setOption(StandardSocketOptions.SO_RCVBUF, 1024);

                Void connect = channel.connect(new InetSocketAddress(IP, PORT)).get();
                //데이터 전송
                channel.write(helloBuffer).get();
                //응답을 기다린후 랜덤번호를 생성하여 서버로 보낸다.
                int count = 0;
                while(channel.read(buffer).get() != -1) {
                    buffer.flip();
                    System.out.println(Charset.defaultCharset().decode(buffer));

                    if(buffer.hasRemaining()) {
                        buffer.compact();
                    } else {
                        buffer.clear();
                    }

                    int random = new Random().nextInt(10000);
                    if(random == 50) {
                        System.out.println("50 was generated! closed socket channel...");
                        break;
                    } else {
                        randomBuffer = ByteBuffer.wrap("random number: ".concat(String.valueOf(random)).concat(" => count: " + count++).getBytes());
                        channel.write(randomBuffer).get();
                    }
                }
            }
        } catch(Exception e) {
            System.err.println(e);
        }
    }

    public static void handler() {
        final int PORT = 5558;
        final String IP = "127.0.0.1";

        try(AsynchronousSocketChannel channel = AsynchronousSocketChannel.open()) {
            if(channel.isOpen()) {
                channel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
                //이 채널의 소켓을 연결한다.
                channel.connect(new InetSocketAddress(IP, PORT), null, new CompletionHandler<Void, Void>() {
                    ByteBuffer helloBuffer = ByteBuffer.wrap("hello".getBytes());
                    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                    ByteBuffer randomBuffer;
                    CharBuffer charBuffer;

                    @Override
                    public void completed(Void result, Void attachment) {
                        try {
                            System.out.println("connected at: " + channel.getRemoteAddress());
                            channel.write(helloBuffer).get();

                            int count = 0;
                            while(channel.read(buffer).get() != -1) {
                                buffer.flip();
                                charBuffer = Charset.defaultCharset().decode(buffer);
                                System.out.println(charBuffer);

                                if(buffer.hasRemaining()) {
                                    buffer.compact();
                                } else {
                                    buffer.clear();
                                }

                                int random = new Random().nextInt(10000);
                                if(random == 50) {
                                    System.out.println("50 was generated! close socket channel!");
                                    break;
                                } else {
                                    randomBuffer = ByteBuffer.wrap(("random number: " + random + " => count: " + count++).getBytes());
                                    channel.write(randomBuffer).get();
                                }
                            }
                        } catch(Exception e) {
                            System.err.println(e);
                        } finally {
                            try { channel.close(); } catch(IOException e) {}
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        throw new UnsupportedOperationException("connection cannot be established!");
                    }
                });
                //대기하는 부분(비동기임으로 이부분이 없으면 프로그램이 끝남)
                System.in.read();
            }

        } catch(Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        //AsyncClient.future();
        AsyncClient.handler();
    }
}
