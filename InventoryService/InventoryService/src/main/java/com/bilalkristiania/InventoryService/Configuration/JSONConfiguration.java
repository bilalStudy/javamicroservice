package com.bilalkristiania.InventoryService.Configuration;

import com.fasterxml.jackson.databind.Module;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JSONConfiguration {

    @Bean
    public Module hibernateModule() {
        return new Hibernate5Module();
    }

}
