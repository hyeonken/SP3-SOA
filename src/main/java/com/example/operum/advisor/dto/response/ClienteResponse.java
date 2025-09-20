package com.example.operum.advisor.dto.response;

import com.example.operum.advisor.domain.enums.RiskProfile;

public class ClienteResponse {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private RiskProfile perfilRisco;
    private boolean consentimentoLgpd;

    public ClienteResponse(Long id, String nome, String cpf, String email, RiskProfile perfilRisco, boolean consentimentoLgpd) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.perfilRisco = perfilRisco;
        this.consentimentoLgpd = consentimentoLgpd;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public RiskProfile getPerfilRisco() {
        return perfilRisco;
    }

    public boolean isConsentimentoLgpd() {
        return consentimentoLgpd;
    }
}
