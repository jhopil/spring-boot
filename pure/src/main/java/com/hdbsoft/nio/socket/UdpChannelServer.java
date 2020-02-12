package com.hdbsoft.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;

public class UdpChannelServer {

    public static void main(String[] args) {
        final int PORT = 5555;
        final String IP = "127.0.0.1";
        final int MAX_PACKET_SIZE = 65507; //udp max packet size = 2byte(65535) - IP header(20)

        ByteBuffer echoText = ByteBuffer.allocate(MAX_PACKET_SIZE);

        try(DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            if(channel.isOpen()) {
                System.out.println("Echo server opened!!");

                channel.setOption(StandardSocketOptions.SO_RCVBUF, 4* 1024);
                channel.setOption(StandardSocketOptions.SO_SNDBUF, 4* 1024);

                channel.bind(new InetSocketAddress(IP, PORT));
                System.out.println("Echo server binded on: " + channel.getLocalAddress());
                System.out.println("Echo server is ready ...");

                while(true) {
                    SocketAddress clientAddress = channel.receive(echoText);
                    echoText.flip();
                    System.out.println("received " + echoText.limit() +" bytes from " + clientAddress.toString());

                    channel.send(echoText, clientAddress);
                    echoText.clear();
                }
            }

        } catch(Exception e) {
            if(e instanceof ClosedChannelException) {
                System.err.println("The channel was unexpected closed..");
            } else if(e instanceof SecurityException) {
                System.err.println("A security exception occured..");
            } else if(e instanceof IOException) {
                System.err.println("An I/O error occured..");
            }
            System.err.println("\n" + e);
        }
    }
}
