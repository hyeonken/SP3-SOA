package com.example.operum.advisor.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro de autenticação JWT que intercepta todas as requisições HTTP. Valida o
 * token JWT presente no header Authorization e configura o SecurityContext.
 * Implementa o padrão Filter Chain do Spring Security para autenticação
 * stateless.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Filtra cada requisição HTTP para validar o token JWT.
     *
     * @param request Requisição HTTP
     * @param response Resposta HTTP
     * @param filterChain Cadeia de filtros
     * @throws ServletException em caso de erro no servlet
     * @throws IOException em caso de erro de I/O
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Ignora filtro JWT para endpoints públicos do Swagger e autenticação
        String path = request.getRequestURI();
        if (path.startsWith("/auth/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/webjars/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Verifica se o header Authorization existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extrai o token JWT do header (remove "Bearer " do início)
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);

            // Valida o token e configura a autenticação no SecurityContext
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token inválido ou expirado - apenas continua sem autenticação
            // O Spring Security retornará 403 automaticamente
        }

        filterChain.doFilter(request, response);
    }
}
