package com.hdbsoft.test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
//        System.out.println("enter key...");
//        int readCnt = System.in.read();
//        System.out.println(readCnt);

        int count = 0;
        final int THREAD_NUM = 50;
        ExecutorService executor = null;
        try {
            while (true) {
                System.out.println("count: " + ++count + " started!!");

                executor = Executors.newFixedThreadPool(THREAD_NUM);

                for (int num = 0; num < THREAD_NUM; ++num) {
                    executor.execute(() -> {
                        System.out.println(Thread.currentThread());
                    });
                }

                TimeUnit.MILLISECONDS.sleep(10);

                executor.shutdownNow();

            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
