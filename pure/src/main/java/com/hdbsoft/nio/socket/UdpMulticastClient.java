package com.hdbsoft.nio.socket;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class UdpMulticastClient {

    public static void main(String[] args) {
        final int PORT = 5556;
        final int MAX_PACKET_SIZE = 65507;
        final String GROUP = "225.4.5.6";

        CharBuffer charBuffer;
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();
        ByteBuffer datetime = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);

        try(DatagramChannel channel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            InetAddress group = InetAddress.getByName(GROUP);
            if(group.isMulticastAddress()) {
                if(channel.isOpen()) {
                    NetworkInterface ne = NetworkInterface.getByName("wlan1");

                    channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                    //채널을 로컬주소에 바인딩한다.
                    channel.bind(new InetSocketAddress(PORT));
                    //멀티캐스트 그룹에 가입하고 데이터그램 수신을 준비한다.
                    MembershipKey key = channel.join(group, ne);
                    //데이터를 기다린다.
                    while(true) {
                        if(key.isValid()) {
                            channel.receive(datetime);
                            datetime.flip();

                            charBuffer = decoder.decode(datetime);
                            System.out.println(charBuffer.toString());
                            datetime.clear();
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch(Exception e) {

        }
    }
}
