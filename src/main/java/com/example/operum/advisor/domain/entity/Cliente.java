package com.example.operum.advisor.domain.entity;

import com.example.operum.advisor.domain.enums.RiskProfile;
import com.example.operum.advisor.domain.vo.Cpf;
import com.example.operum.advisor.util.crypto.EncryptedStringAttributeConverter;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    @Convert(converter = EncryptedStringAttributeConverter.class)
    private String nome;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "cpf", nullable = false, unique = true, length = 11))
    private Cpf cpf;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    @Convert(converter = EncryptedStringAttributeConverter.class)
    private String email;

    @Column(name = "consentimento_lgpd", nullable = false)
    private boolean consentimentoLgpd;

    @Column(name = "objetivos", length = 500)
    private String objetivos;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_risco", nullable = false, length = 20)
    private RiskProfile perfilRisco;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void onCreate() {
        criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public void setCpf(Cpf cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isConsentimentoLgpd() {
        return consentimentoLgpd;
    }

    public void setConsentimentoLgpd(boolean consentimentoLgpd) {
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

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
