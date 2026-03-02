package com.ufund.api.ufundapi;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public Random random() {
        return new Random();
    }
}
