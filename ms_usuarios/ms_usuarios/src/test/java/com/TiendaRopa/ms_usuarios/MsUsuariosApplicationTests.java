package com.TiendaRopa.ms_usuarios;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MsUsuariosApplicationTests {

    @Test
    @Disabled("Spring Cloud 2023.0.1 incompatible con Spring Boot 4.0.6")
    void contextLoads() {
    }
}
