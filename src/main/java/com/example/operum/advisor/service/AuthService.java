package com.example.operum.advisor.service;

import com.example.operum.advisor.dto.request.LoginRequest;
import com.example.operum.advisor.dto.response.AuthResponse;

/**
 * Interface para serviço de autenticação. Define operações relacionadas à
 * autenticação de usuários e geração de tokens JWT.
 */
public interface AuthService {

    /**
     * Autentica um usuário e gera um token JWT.
     *
     * @param request Credenciais do usuário
     * @return Resposta contendo o token JWT e informações de autenticação
     */
    AuthResponse authenticate(LoginRequest request);
}
