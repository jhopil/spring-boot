package com.hdbsoft.spring.jms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class JmsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JmsApplication.class, args);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        System.out.println("sending an email message..");
        jmsTemplate.convertAndSend("mailbox", new Email("jhopil@daum.net", "hello"));
    }
}
