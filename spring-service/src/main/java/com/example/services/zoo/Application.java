package com.example.services.zoo;

import com.example.zoo.messageconverter.JsonMessageConverter;
import com.example.zoo.messageconverter.ThriftJsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {
    @Bean
    public HttpMessageConverters customConverters() {
        return new HttpMessageConverters(false, Arrays.asList(new ThriftJsonMessageConverter(), new JsonMessageConverter()));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
