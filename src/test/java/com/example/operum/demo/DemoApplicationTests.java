package com.example.operum.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.operum.advisor.OperumAdvisorApplication;

/**
 * Teste de contexto da aplicação. Verifica se o contexto Spring inicializa
 * corretamente.
 */
@SpringBootTest(classes = OperumAdvisorApplication.class)
class DemoApplicationTests {

    @Test
    void contextLoads() {
        // Verifica se o contexto Spring carrega sem erros
    }

}
