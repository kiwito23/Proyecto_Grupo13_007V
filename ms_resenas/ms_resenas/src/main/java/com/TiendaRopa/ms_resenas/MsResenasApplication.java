package com.TiendaRopa.ms_resenas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class MsResenasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsResenasApplication.class, args);
    }
}