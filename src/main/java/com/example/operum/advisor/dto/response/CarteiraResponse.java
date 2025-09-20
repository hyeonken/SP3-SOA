package com.example.operum.advisor.dto.response;

import com.example.operum.advisor.domain.enums.RiskProfile;
import java.math.BigDecimal;
import java.util.List;

public class CarteiraResponse {

    private Long id;
    private String nome;
    private String descricao;
    private RiskProfile perfilRisco;
    private BigDecimal retornoEsperado;
    private BigDecimal riscoEstimado;
    private List<ItemCarteiraResponse> itens;

    public CarteiraResponse(Long id, String nome, String descricao, RiskProfile perfilRisco, BigDecimal retornoEsperado, BigDecimal riscoEstimado, List<ItemCarteiraResponse> itens) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.perfilRisco = perfilRisco;
        this.retornoEsperado = retornoEsperado;
        this.riscoEstimado = riscoEstimado;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public RiskProfile getPerfilRisco() {
        return perfilRisco;
    }

    public BigDecimal getRetornoEsperado() {
        return retornoEsperado;
    }

    public BigDecimal getRiscoEstimado() {
        return riscoEstimado;
    }

    public List<ItemCarteiraResponse> getItens() {
        return itens;
    }
}
