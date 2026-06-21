package com.TiendaRopa.ms_envios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class MsEnviosApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsEnviosApplication.class, args);
    }
}