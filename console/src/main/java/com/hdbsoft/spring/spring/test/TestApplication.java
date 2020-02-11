package com.hdbsoft.spring.spring.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestApplication {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HelloConfig.class);
        Hello hello = context.getBean(Hello.class);
        //Hello hello1 = (Hello) context.getBean("hello");
        System.out.println(hello);

        Hi hi = context.getBean(Hi.class);
        System.out.println(hi);



    }
}
