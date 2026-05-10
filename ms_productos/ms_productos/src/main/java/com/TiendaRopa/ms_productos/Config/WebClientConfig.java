package com.TiendaRopa.ms_productos.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.categorias.url}")
    private String msCategoriasUrl;

    @Bean
    public WebClient webClientCategorias(WebClient.Builder builder) {
        return builder.baseUrl(msCategoriasUrl).build();
    }
}
