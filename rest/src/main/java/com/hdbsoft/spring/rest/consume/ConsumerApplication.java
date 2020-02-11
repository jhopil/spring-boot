package com.hdbsoft.spring.rest.consume;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumerApplication {

    private static Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        System.out.println(builder);
        return builder.build();
    }

//    @Autowired
//    private RestTemplateBuilder builder;

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
          Quote quote = restTemplate.getForObject(
                  "https://gturnquist-quoters.cfapps.io/api/random", Quote.class);
          logger.info(quote.toString());
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
