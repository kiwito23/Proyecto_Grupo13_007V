package com.TiendaRopa.ms_inventario.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClienteConfig {

    @Value("${ms_productos.url}")
    private String msProductosUrl;

    @Bean
    public WebClient webClientProductos() {
        return WebClient.builder()
                .baseUrl(msProductosUrl)
                .build();
    }

}
