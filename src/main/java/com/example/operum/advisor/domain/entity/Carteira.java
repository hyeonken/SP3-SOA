package com.example.operum.advisor.domain.entity;

import com.example.operum.advisor.domain.enums.RiskProfile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carteira")
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "descricao", length = 300)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_risco", nullable = false, length = 20)
    private RiskProfile perfilRisco;

    @Column(name = "retorno_esperado", precision = 10, scale = 2)
    private BigDecimal retornoEsperado;

    @Column(name = "risco_estimado", precision = 10, scale = 2)
    private BigDecimal riscoEstimado;

    @OneToMany(mappedBy = "carteira", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarteira> itens = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public RiskProfile getPerfilRisco() {
        return perfilRisco;
    }

    public void setPerfilRisco(RiskProfile perfilRisco) {
        this.perfilRisco = perfilRisco;
    }

    public BigDecimal getRetornoEsperado() {
        return retornoEsperado;
    }

    public void setRetornoEsperado(BigDecimal retornoEsperado) {
        this.retornoEsperado = retornoEsperado;
    }

    public BigDecimal getRiscoEstimado() {
        return riscoEstimado;
    }

    public void setRiscoEstimado(BigDecimal riscoEstimado) {
        this.riscoEstimado = riscoEstimado;
    }

    public List<ItemCarteira> getItens() {
        return itens;
    }
}
