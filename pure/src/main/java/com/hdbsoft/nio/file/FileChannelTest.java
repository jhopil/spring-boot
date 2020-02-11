package com.hdbsoft.nio.file;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FileChannelTest {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("./data/test3.txt");

        FileChannel channel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.READ, StandardOpenOption.WRITE));

        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        Charset charset = Charset.defaultCharset();
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer charBuffer = decoder.decode(buffer);

        String content = charBuffer.toString();
        System.out.println(content);

        buffer.clear();
        channel.close();

    }
}
