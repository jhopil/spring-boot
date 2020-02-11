package com.hdbsoft.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class EchoServer {

    public static void main(String[] args) throws IOException {

        final int PORT = 5555;
        final String IP = "127.0.0.1";

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        try(ServerSocketChannel serverChannel = ServerSocketChannel.open()){

            if (serverChannel.isOpen()) {
                serverChannel.configureBlocking(true); //blocking mode

                serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
                serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

                serverChannel.bind(new InetSocketAddress(IP, PORT));

                System.out.println("waiting for connections...");

                while (true) {
                    try(SocketChannel channel = serverChannel.accept()) {
                        System.out.println("incomming connection from: " + channel.getRemoteAddress());

                        while(channel.read(buffer) != -1) {
                            buffer.flip();
                            channel.write(buffer);

                            buffer.rewind();
                            System.out.println(Charset.defaultCharset().decode(buffer));

                            if(buffer.hasRemaining()) {
                                buffer.compact();
                            } else {
                                buffer.clear();
                            }
                        }

                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}
