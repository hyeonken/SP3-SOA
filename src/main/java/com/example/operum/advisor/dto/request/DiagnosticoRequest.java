package com.example.operum.advisor.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DiagnosticoRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    @Min(0)
    private Integer scoreRisco;

    @Size(max = 500)
    private String objetivosComplementares;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getScoreRisco() {
        return scoreRisco;
    }

    public void setScoreRisco(Integer scoreRisco) {
        this.scoreRisco = scoreRisco;
    }

    public String getObjetivosComplementares() {
        return objetivosComplementares;
    }

    public void setObjetivosComplementares(String objetivosComplementares) {
        this.objetivosComplementares = objetivosComplementares;
    }
}
