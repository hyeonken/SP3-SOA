package com.example.operum.advisor.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de login/autenticação. Contém as credenciais do usuário
 * para geração do token JWT.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Credenciais para autenticação do usuário")
public class LoginRequest {

    @Schema(
            description = "Nome de usuário para autenticação",
            example = "operum",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Username é obrigatório")
    private String username;

    @Schema(
            description = "Senha do usuário",
            example = "operum123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "password"
    )
    @NotBlank(message = "Password é obrigatório")
    private String password;
}
