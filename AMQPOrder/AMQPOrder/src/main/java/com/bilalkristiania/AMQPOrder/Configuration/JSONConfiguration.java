package com.bilalkristiania.AMQPOrder.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSONConfiguration {

    @Bean
    public Module hibernateModule() {
        return new Hibernate5Module();
    }

}