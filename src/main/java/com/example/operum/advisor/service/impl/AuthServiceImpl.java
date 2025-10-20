package com.example.operum.advisor.service.impl;

import com.example.operum.advisor.dto.request.LoginRequest;
import com.example.operum.advisor.dto.response.AuthResponse;
import com.example.operum.advisor.security.JwtService;
import com.example.operum.advisor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço de autenticação. Gerencia o processo de login e
 * geração de tokens JWT.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Autentica o usuário através das credenciais fornecidas e gera um token
     * JWT.
     *
     * @param request Credenciais do usuário (username e password)
     * @return Resposta contendo o token JWT e informações de autenticação
     * @throws org.springframework.security.core.AuthenticationException se as
     * credenciais forem inválidas
     */
    @Override
    public AuthResponse authenticate(LoginRequest request) {
        // Autentica as credenciais usando o AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Carrega os detalhes do usuário autenticado
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Gera o token JWT
        String jwtToken = jwtService.generateToken(userDetails);

        // Retorna a resposta com o token
        return AuthResponse.of(jwtToken, userDetails.getUsername(), jwtExpiration);
    }
}
