package com.example.operum.advisor.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para autenticação. Contém o token JWT gerado e informações
 * adicionais sobre a autenticação.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta de autenticação contendo o token JWT")
public class AuthResponse {

    @Schema(
            description = "Token JWT para autenticação nas requisições",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJvcGVydW0iLCJpYXQiOjE2OTg5MDAwMDAsImV4cCI6MTY5ODk4NjQwMH0.abc123..."
    )
    private String token;

    @Schema(
            description = "Tipo do token (sempre 'Bearer')",
            example = "Bearer"
    )
    private String type;

    @Schema(
            description = "Tempo de expiração do token em milissegundos",
            example = "86400000"
    )
    private Long expiresIn;

    @Schema(
            description = "Nome do usuário autenticado",
            example = "operum"
    )
    private String username;

    /**
     * Cria uma resposta de autenticação com valores padrão.
     *
     * @param token Token JWT gerado
     * @param username Nome do usuário autenticado
     * @param expiresIn Tempo de expiração em milissegundos
     * @return AuthResponse configurado
     */
    public static AuthResponse of(String token, String username, Long expiresIn) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .username(username)
                .expiresIn(expiresIn)
                .build();
    }
}
