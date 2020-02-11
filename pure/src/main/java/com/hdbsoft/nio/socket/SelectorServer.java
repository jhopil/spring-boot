package com.hdbsoft.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class SelectorServer {

    private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
    private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

    private void start() {
        final int PORT = 5555;

        try(Selector selector = Selector.open();
            ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            if((serverChannel.isOpen()) && (selector.isOpen())) {
                serverChannel.configureBlocking(false); //non-blocking

                serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 256 * 1024);
                serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);

                serverChannel.bind(new InetSocketAddress(PORT));

                serverChannel.register(selector, SelectionKey.OP_ACCEPT);

                System.out.println("waiting for connections ...");

                while(true) {
                    selector.select();

                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                    while(keys.hasNext()) {
                        SelectionKey key = keys.next();
                        //같은 키가 반복해서 오는것을 막기위해 가져온 키는 제거한다.
                        keys.remove();

                        if(key.isValid() == false) {
                            continue;
                        }

                        if(key.isAcceptable()) {
                            acceptOp(key, selector);
                        } else if(key.isReadable()) {
                            readOp(key);
                        } else if(key.isWritable()) {
                            writeOp(key);
                        }
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptOp(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);

        System.out.println("incomming connection from => " + channel.getRemoteAddress());

        channel.write(ByteBuffer.wrap("hello!\n".getBytes()));

        // I/O 처리를 위해 채널을 셀렉터에 등록한다.
        keepDataTrack.put(channel, new ArrayList<byte[]>());
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void readOp(SelectionKey key) {
        try {
            SocketChannel channel = (SocketChannel)key.channel();
            buffer.clear();

            int numRead = -1;
            try {
                numRead = channel.read(buffer);
            } catch(IOException e) {
                e.printStackTrace();
            }

            if(numRead == -1) {
                keepDataTrack.remove(channel);
                System.out.println("connection closed by " + channel.getRemoteAddress());
                channel.close();
                key.cancel();
                return;
            }

            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            System.out.println(new String(data).concat(" from ") + channel.getRemoteAddress());

            doEchoJob(key, data);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void writeOp(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel)key.channel();

        List<byte[]> channelData = keepDataTrack.get(channel);
        Iterator<byte[]> its = channelData.iterator();

        while(its.hasNext()) {
            byte[] it = its.next();
            its.remove();
            channel.write(ByteBuffer.wrap(it));
        }

        key.interestOps(SelectionKey.OP_READ); // ?? 확인필요
    }

    private void doEchoJob(SelectionKey key, byte[] data) {
        SocketChannel channel = (SocketChannel)key.channel();
        List<byte[]> channelData = keepDataTrack.get(channel);
        channelData.add(data);

        key.interestOps(SelectionKey.OP_WRITE);
    }

    public static void main(String[] args) {
        SelectorServer server = new SelectorServer();
        server.start();
    }
}
