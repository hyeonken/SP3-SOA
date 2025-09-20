package com.example.operum.advisor.dto.response;

import java.math.BigDecimal;

public class ItemCarteiraResponse {

    private String codigoAtivo;
    private String nomeAtivo;
    private BigDecimal percentual;

    public ItemCarteiraResponse(String codigoAtivo, String nomeAtivo, BigDecimal percentual) {
        this.codigoAtivo = codigoAtivo;
        this.nomeAtivo = nomeAtivo;
        this.percentual = percentual;
    }

    public String getCodigoAtivo() {
        return codigoAtivo;
    }

    public String getNomeAtivo() {
        return nomeAtivo;
    }

    public BigDecimal getPercentual() {
        return percentual;
    }
}
