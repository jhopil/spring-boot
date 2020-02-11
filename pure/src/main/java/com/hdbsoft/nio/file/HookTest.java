package com.hdbsoft.nio.file;

public class HookTest {

    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                System.out.println("shutdown-hook activated...");

                //do somthing here..

                System.out.println("shutdown-hook executed...");
            }
        });

        System.out.println("sleeping..");
        Thread.sleep(3000);
        System.out.println("sleeping end..");
    }
}
