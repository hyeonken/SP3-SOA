package com.example.operum.advisor.domain.entity;

import com.example.operum.advisor.domain.enums.RiskProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ativo")
public class Ativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "classe", nullable = false, length = 50)
    private String classe;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil_risco", nullable = false, length = 20)
    private RiskProfile perfilRisco;

    @Column(name = "retorno_esperado", precision = 10, scale = 2)
    private BigDecimal retornoEsperado;

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
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
}
