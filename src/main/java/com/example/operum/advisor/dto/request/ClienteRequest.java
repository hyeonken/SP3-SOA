package com.example.operum.advisor.dto.request;

import com.example.operum.advisor.domain.enums.RiskProfile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteRequest {

    @NotBlank
    @Size(max = 120)
    private String nome;

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 numeros")
    private String cpf;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    @NotNull
    private Boolean consentimentoLgpd;

    @Size(max = 500)
    private String objetivos;

    @NotNull
    private RiskProfile perfilRisco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getConsentimentoLgpd() {
        return consentimentoLgpd;
    }

    public void setConsentimentoLgpd(Boolean consentimentoLgpd) {
        this.consentimentoLgpd = consentimentoLgpd;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public RiskProfile getPerfilRisco() {
        return perfilRisco;
    }

    public void setPerfilRisco(RiskProfile perfilRisco) {
        this.perfilRisco = perfilRisco;
    }
}
