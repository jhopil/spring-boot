package com.hdbsoft.nio.file;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.*;

public class AsyncFileTest {

    public static void future() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String encoding = System.getProperty("file.encoding");

        Path path = Paths.get("./data","test1.txt");
        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {

            Future<Integer> result = channel.read(buffer, 0);
            while(result.isDone() == false) {
                System.out.println("do something else while reading ...");
            }
            System.out.println("read done: " + result.isDone());
            System.out.println("read bytes: " + result.get());

        } catch(Exception e) {
            System.err.println(e);
        }

        buffer.flip();
        System.out.println(Charset.defaultCharset().decode(buffer));
        buffer.clear();
    }

    public static void handler() {
        Thread current = Thread.currentThread();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Path path = Paths.get("./data", "test2.txt");

        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    buffer.flip();
                    System.out.println(Charset.defaultCharset().decode(attachment));
                    buffer.clear();
                    System.out.print("read bytes: " + result);

                    current.interrupt();
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    buffer.flip();
                    System.out.println(Charset.defaultCharset().decode(attachment));
                    buffer.clear();
                    System.out.println("Error: " + exc);

                    current.interrupt();
                }
            });
            System.out.println("\nwaiting for reading operation to end...\n");

            try {
                current.join();
            } catch(InterruptedException e) {}

            System.out.println("\n\nclose everything and leave...");

        } catch(Exception e) {
            System.err.println(e);
        }
    }

    public static void executor() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<ByteBuffer>> list = new ArrayList<>();
        Path path = Paths.get("./data", "test3.txt");

        try(AsynchronousFileChannel channel = AsynchronousFileChannel.open(path, EnumSet.of(StandardOpenOption.READ), executor)) {
            for(int i = 0; i < 50; ++i) {
                Callable<ByteBuffer> worker = new Callable<ByteBuffer>() {
                    @Override
                    public ByteBuffer call() throws Exception {
                        ByteBuffer buffer = ByteBuffer.allocateDirect(ThreadLocalRandom.current().nextInt(100, 200));
                        channel.read(buffer, ThreadLocalRandom.current().nextInt(0, 100));
                        return buffer;
                    }
                };

                Future<ByteBuffer> future = executor.submit(worker);
                list.add(future);
            }
            //새로운 스레드를 수락하지 않고 큐에 있는 기존 스레드를 모두 끝낸다.
            executor.shutdown();

            int count = 0;
            //스레드가 모두 끝날때까지 대기한다.
            while(executor.isShutdown() == false) {
                System.out.println("counting sheep while filling up some buffer... count: " + count++);
            }

            System.out.println("\nDone! buffers:\n");
            for(Future<ByteBuffer> future : list) {
                ByteBuffer buffer = future.get();
                System.out.println("\n\n" + buffer);
                System.out.println("-----------------------------------------------");

                buffer.flip();
                System.out.println(Charset.defaultCharset().decode(buffer));
                buffer.clear();
            }

        } catch(Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        AsyncFileTest.future();
        AsyncFileTest.handler();
        AsyncFileTest.executor();
    }
}
