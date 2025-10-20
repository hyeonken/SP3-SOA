package com.example.operum.advisor.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.operum.advisor.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Testes de integração para AuthController.
 *
 * Valida: - Login com credenciais válidas → 200 + JWT token - Login com
 * credenciais inválidas → 401 - Acesso a endpoints protegidos sem token → 403 -
 * Acesso a endpoints protegidos com token válido → 200 - Integração completa de
 * JWT authentication
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthController - Testes de Integração")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve fazer login com credenciais válidas e retornar JWT token")
    void deveFazerLoginComSucesso() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("operum");
        loginRequest.setPassword("operum123");

        // Act & Assert
        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.username").value("operum"))
                .andExpect(jsonPath("$.expiresIn").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("token");
        assertThat(responseBody).contains("Bearer");
    }

    @Test
    @DisplayName("Deve retornar 401 para credenciais inválidas")
    void deveRetornar401ParaCredenciaisInvalidas() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("senhaErrada");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 401 para usuário inexistente")
    void deveRetornar401ParaUsuarioInexistente() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("usuarioQueNaoExiste");
        loginRequest.setPassword("qualquerSenha");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 403 ao acessar endpoint protegido sem token")
    void deveRetornar403SemToken() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 403 para token inválido")
    void deveRetornar403ParaTokenInvalido() throws Exception {
        // Arrange - Token JWT com formato válido mas assinatura inválida
        String tokenInvalido = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0ZSIsImV4cCI6OTk5OTk5OTk5OX0.invalid_signature_here_123456789";

        // Act & Assert
        mockMvc.perform(get("/clientes")
                .header("Authorization", "Bearer " + tokenInvalido))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve permitir acesso a endpoint protegido com token válido")
    void devePermitirAcessoComTokenValido() throws Exception {
        // Arrange - Primeiro faz login para obter token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("operum");
        loginRequest.setPassword("operum123");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String loginResponse = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(loginResponse).get("token").asText();

        // Act & Assert - Usa token para acessar endpoint protegido
        mockMvc.perform(get("/clientes")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve aceitar requisição sem token para endpoint público /auth/login")
    void devePermitirAcessoAEndpointPublico() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("operum");
        loginRequest.setPassword("operum123");

        // Act & Assert - Login não requer autenticação
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios no login")
    void deveValidarCamposObrigatorios() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        // Não define username nem password

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError());
    }
}
