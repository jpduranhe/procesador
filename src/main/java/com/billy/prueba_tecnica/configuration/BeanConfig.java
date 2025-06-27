package com.billy.prueba_tecnica.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.DoubleAccumulator;

@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Automatically register modules like JavaTimeModule
        return objectMapper;
    }
    @Bean
    public ConcurrentMap<String, DoubleAccumulator> dataForHml() {
        return new ConcurrentHashMap<>();
    }
}
