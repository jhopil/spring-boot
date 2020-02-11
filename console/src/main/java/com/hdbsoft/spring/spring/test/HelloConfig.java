package com.hdbsoft.spring.spring.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfig {

//    @Autowired
//    private HelloPrint helloPrint;

    //@Autowired
    //private Student student;

    @Bean
    public Hello hello(HelloPrint helloPrint) {
        Hello hello = new Hello();
        hello.setName("hello world");
        hello.print(helloPrint);
        return hello;
    }

    @Bean
    public HelloPrint helloPrint() {
        HelloPrint helloPrint = new HelloPrint();
        return helloPrint;
    }

    @Bean
    public Hi hi(Hello hello) {
        Hi hi = new Hi();
        hi.setHello(hello);

        //System.out.println(student.toString());
        return hi;
    }


}
