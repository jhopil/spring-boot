package com.hdbsoft.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncServer {

    public static void future() {
        final int PORT = 5557;
        final String IP = "127.0.0.1";
        ExecutorService executor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());

        try(AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open()) {
            if(serverChannel.isOpen()) {
                serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 1024);
                serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                serverChannel.bind(new InetSocketAddress(PORT));

                System.out.println("waiting for connections...");
                while (true) {
                    Future<AsynchronousSocketChannel> future = serverChannel.accept();
                    try {
                        AsynchronousSocketChannel clientChannel = future.get();
                        Callable<String> worker = new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                String host = clientChannel.getRemoteAddress().toString();
                                System.out.println("incomming connection from: " + host);

                                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                                while(clientChannel.read(buffer).get() != -1) {
                                    buffer.flip();
                                    clientChannel.write(buffer).get();
                                    if(buffer.hasRemaining()) {
                                        buffer.compact();
                                    } else {
                                        buffer.clear();
                                    }
                                }
                                clientChannel.close();
                                System.out.println(host + " was successfully served!");
                                return host;
                            }
                        };
                        executor.submit(worker);

                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("\nserver is shutting down...");
                        //새로운 스레드를 수락하지 못하게 하고 큐에 있는 스레드를 모두 끝낸다.
                        executor.shutdown();
                        //모든 스레드가 끝날때까지 기다린다.
                        while(!executor.isTerminated()) {
                        }
                        break;
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

        try(AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open()) {
            if(serverChannel.isOpen()) {
                serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

                serverChannel.bind(new InetSocketAddress(PORT));
                System.out.println("waiting for connections:");

                serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
                    ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                    @Override
                    public void completed(AsynchronousSocketChannel result, Void attachment) {
                        serverChannel.accept(null, this);

                        try {
                            System.out.println("incomming connection from: " + result.getRemoteAddress());

                            while (result.read(buffer).get() != -1) {
                                buffer.flip();
                                //System.out.println(buffer);
                                result.write(buffer).get();
                                if(buffer.hasRemaining()) {
                                    buffer.compact();
                                } else {
                                    buffer.clear();
                                }
                            }
                        } catch(Exception e) {
                            System.err.println(e);
                        } finally {
                            try { result.close(); } catch(IOException e) {}
                        }
                    }

                    @Override
                    public void failed(Throwable exc, Void attachment) {
                        serverChannel.accept(null, this);
                        throw new UnsupportedOperationException("cannot accept connections!");
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
        //AsyncServer.future();
        AsyncServer.handler();
    }
}
