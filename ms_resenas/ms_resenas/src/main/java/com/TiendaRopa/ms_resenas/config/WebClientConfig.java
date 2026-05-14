package com.TiendaRopa.ms_resenas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.productos.url}")
    private String msProductosUrl;

    @Bean
    public WebClient webClientProductos(WebClient.Builder builder) {
        return builder.baseUrl(msProductosUrl).build();
    }
}