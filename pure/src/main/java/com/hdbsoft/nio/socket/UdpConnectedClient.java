package com.hdbsoft.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * connect to UdpChannelServer
 */
public class UdpConnectedClient {

    public static void main(String[] args) {
        final int PORT = 5555;
        final String IP = "127.0.0.1";
        final int MAX_PACKET_SIZE = 65507; //udp max packet size = 2byte(65535) - IP header(20)

        CharBuffer charBuffer;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();

        ByteBuffer textEcho = ByteBuffer.wrap("echo this : I'm big and ugly server!".getBytes());
        ByteBuffer echoedText = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);

        try(DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            channel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            channel.setOption(StandardSocketOptions.SO_SNDBUF, 4 * 1024);

            if(channel.isOpen()) {
                channel.connect(new InetSocketAddress(IP, PORT));

                if(channel.isConnected()) {
                    int sentLen = channel.write(textEcho);
                    System.out.println("sent " + sentLen + " bytes to the Echo server");

                    channel.read(echoedText);
                    echoedText.flip();
                    charBuffer = decoder.decode(echoedText);
                    System.out.println(charBuffer.toString());
                    echoedText.clear();
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
