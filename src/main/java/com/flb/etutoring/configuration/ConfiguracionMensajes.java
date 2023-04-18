package com.flb.etutoring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfiguracionMensajes {
    @Bean
    public RestTemplate gRestTemplate() {
        return new RestTemplate();
    }
}
