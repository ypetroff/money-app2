package com.example.moneyapp2.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class UtilConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(mappingContext ->
                        LocalDateTime.parse(mappingContext.getSource(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                String.class,
                LocalDateTime.class);

        return modelMapper;
    }
}
