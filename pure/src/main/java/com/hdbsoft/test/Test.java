package com.hdbsoft.test;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        System.out.println("enter key...");
        int readCnt = System.in.read();
        System.out.println(readCnt);
    }
}
