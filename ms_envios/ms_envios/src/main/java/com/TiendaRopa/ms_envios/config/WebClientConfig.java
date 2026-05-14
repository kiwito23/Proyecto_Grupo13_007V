package com.TiendaRopa.ms_envios.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.pedidos.url}")
    private String msPedidosUrl;

    @Bean
    public WebClient webClientPedidos(WebClient.Builder builder) {
        return builder.baseUrl(msPedidosUrl).build();
    }
}