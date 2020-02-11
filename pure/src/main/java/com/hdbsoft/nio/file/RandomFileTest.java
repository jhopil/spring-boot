package com.hdbsoft.nio.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class RandomFileTest {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("./data").resolve("test3.txt"); // => ./data/test3.txt
        System.out.println(path);

        SeekableByteChannel channel = Files.newByteChannel(path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));

        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.clear();

        //read
        while(channel.read(buffer) > 0) {
            buffer.flip();

//            byte[] buf = buffer.array();
//            String str = new String(buf, 0, buffer.limit());
//            System.out.print(str);
            String data = Charset.forName("utf-8").decode(buffer).toString();
            System.out.print(data);

            buffer.clear();
            buffer.rewind();
        }

        //write
        buffer = ByteBuffer.wrap("테스트 데이터입니다.!!\n".getBytes());
        channel.write(buffer);
        buffer.clear();

        channel.close();
    }
}
