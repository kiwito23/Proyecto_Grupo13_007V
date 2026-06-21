package com.TiendaRopa.ms_tallas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class MsTallasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsTallasApplication.class, args);
    }
}