package com.example.operum.advisor.domain.entity;

import com.example.operum.advisor.domain.enums.RiskProfile;
import com.example.operum.advisor.util.crypto.EncryptedStringAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "diagnostico")
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_risco", nullable = false, length = 20)
    private RiskProfile perfilRisco;

    @Column(name = "score_risco", nullable = false)
    private Integer scoreRisco;

    @Column(name = "objetivos", length = 500)
    private String objetivos;

    @Column(name = "recomendacao_geral", length = 500)
    @Convert(converter = EncryptedStringAttributeConverter.class)
    private String recomendacaoGeral;

    @Column(name = "gerado_em", nullable = false)
    private LocalDateTime geradoEm;

    @PrePersist
    void onCreate() {
        geradoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public RiskProfile getPerfilRisco() {
        return perfilRisco;
    }

    public void setPerfilRisco(RiskProfile perfilRisco) {
        this.perfilRisco = perfilRisco;
    }

    public Integer getScoreRisco() {
        return scoreRisco;
    }

    public void setScoreRisco(Integer scoreRisco) {
        this.scoreRisco = scoreRisco;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getRecomendacaoGeral() {
        return recomendacaoGeral;
    }

    public void setRecomendacaoGeral(String recomendacaoGeral) {
        this.recomendacaoGeral = recomendacaoGeral;
    }

    public LocalDateTime getGeradoEm() {
        return geradoEm;
    }
}
