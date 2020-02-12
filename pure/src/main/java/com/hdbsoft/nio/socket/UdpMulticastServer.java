package com.hdbsoft.nio.socket;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UdpMulticastServer {

    public static void main(String[] args) {
        final int PORT = 5556;
        final String GROUP = "225.4.5.6";

        ByteBuffer datetime;

        try(DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            if(channel.isOpen()) {
                //get teh network interface used for multicast
                NetworkInterface ne = NetworkInterface.getByName("wlan1");

                channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, ne);
                channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

                channel.bind(new InetSocketAddress(PORT));
                System.out.println("data-time server is ready... ");

                while(true) {
                    try {
                        TimeUnit.SECONDS.sleep(10); //10 sec
                    } catch(InterruptedException ex) {}

                    System.out.println("sending data...");
                    datetime = ByteBuffer.wrap(new Date().toString().getBytes());

                    channel.send(datetime, new InetSocketAddress(InetAddress.getByName(GROUP), PORT));
                }
            }

        } catch(Exception e) {
            System.err.println(e);
        }
    }
}
