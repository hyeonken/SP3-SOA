package com.example.operum.advisor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.operum.advisor.dto.request.LoginRequest;
import com.example.operum.advisor.dto.response.AuthResponse;
import com.example.operum.advisor.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller responsável pelos endpoints de autenticação. Gerencia o processo
 * de login e geração de tokens JWT.
 */
@Tag(name = "Autenticação", description = "Endpoints para autenticação e geração de tokens JWT")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para autenticação de usuários. Recebe credenciais (username e
     * password) e retorna um token JWT válido.
     *
     * @param request Credenciais do usuário
     * @return Token JWT e informações de autenticação
     */
    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica o usuário com username e password, retornando um token JWT Bearer válido por 24 horas"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Autenticação realizada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Credenciais inválidas"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Dados de requisição inválidos"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}
