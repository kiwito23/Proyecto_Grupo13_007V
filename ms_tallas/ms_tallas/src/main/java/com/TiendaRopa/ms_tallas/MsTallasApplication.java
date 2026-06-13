package com.TiendaRopa.ms_tallas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
public class MsTallasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsTallasApplication.class, args);
    }
}