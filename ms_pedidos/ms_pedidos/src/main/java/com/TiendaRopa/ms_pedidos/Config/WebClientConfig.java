package com.TiendaRopa.ms_pedidos.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${ms.usuarios.url}")
    private String msUsuariosUrl;

    @Value("${ms.carrito.url}")
    private String msCarritoUrl;

    @Value("${ms.inventario.url}")
    private String msInventarioUrl;

    @Value("${ms.pagos.url}")
    private String msPagosUrl;

    @Value("${ms.envios.url}")
    private String msEnviosUrl;

    @Bean
    public WebClient webClientUsuarios() {
        return WebClient.builder().baseUrl(msUsuariosUrl).build();
    }

    @Bean
    public WebClient webClientCarrito() {
        return WebClient.builder().baseUrl(msCarritoUrl).build();
    }

    @Bean
    public WebClient webClientInventario() {
        return WebClient.builder().baseUrl(msInventarioUrl).build();
    }

    @Bean
    public WebClient webClientPagos() {
        return WebClient.builder().baseUrl(msPagosUrl).build();
    }

    @Bean
    public WebClient webClientEnvios() {
        return WebClient.builder().baseUrl(msEnviosUrl).build();
    }

}
