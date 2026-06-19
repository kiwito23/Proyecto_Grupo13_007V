package com.TiendaRopa.ms_productos;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled("Spring Cloud 2023.0.1 incompatible con Spring Boot 4.0.6")
@SpringBootTest
@ActiveProfiles("test")
class MsProductosApplicationTests {

    @Test
    void contextLoads() {
    }
}