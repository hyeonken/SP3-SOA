package com.example.operum.advisor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.operum.advisor.security.JwtAuthenticationFilter;

/**
 * Configuração de segurança da aplicação. Implementa autenticação JWT stateless
 * com BCrypt para criptografia de senhas. Aplica os princípios de segurança com
 * filtros personalizados e gerenciamento de sessão.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Configura a cadeia de filtros de segurança HTTP. Define políticas de
     * autenticação, autorização e gerenciamento de sessão stateless.
     *
     * @param http HttpSecurity para configuração
     * @param authProvider Provider de autenticação
     * @param jwtAuthFilter Filtro JWT
     * @return SecurityFilterChain configurado
     * @throws Exception em caso de erro na configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authProvider,
            JwtAuthenticationFilter jwtAuthFilter
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sem autenticação)
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/v3/api-docs").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/swagger-resources/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                // Todos os outros endpoints requerem autenticação
                .anyRequest().authenticated()
                )
                // Configuração stateless (sem sessão no servidor)
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Adiciona o filtro JWT antes do filtro de autenticação padrão
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configura o UserDetailsService com usuário em memória. Em produção, deve
     * ser substituído por implementação com banco de dados.
     *
     * @param username Username configurado em application.properties
     * @param password Password configurado em application.properties
     * @param passwordEncoder Encoder para criptografia de senha
     * @return UserDetailsService com usuário configurado
     */
    @Bean
    public UserDetailsService userDetailsService(
            @Value("${app.security.username}") String username,
            @Value("${app.security.password}") String password,
            PasswordEncoder passwordEncoder
    ) {
        UserDetails user = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Configura o provider de autenticação com UserDetailsService e
     * PasswordEncoder.
     *
     * @param userDetailsService Serviço de detalhes do usuário
     * @param passwordEncoder Encoder de senha
     * @return AuthenticationProvider configurado
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        @SuppressWarnings("deprecation")
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    /**
     * Expõe o AuthenticationManager como bean para uso na autenticação.
     *
     * @param config Configuração de autenticação
     * @return AuthenticationManager
     * @throws Exception em caso de erro na configuração
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura o encoder de senhas usando BCrypt. BCrypt é um algoritmo de
     * hash adaptativo com salt automático, recomendado para armazenamento
     * seguro de senhas.
     *
     * @return PasswordEncoder BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
