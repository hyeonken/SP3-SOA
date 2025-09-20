package com.example.operum.advisor.dto.response;

import com.example.operum.advisor.domain.enums.RiskProfile;
import java.time.LocalDateTime;

public class DiagnosticoResponse {

    private Long id;
    private Long clienteId;
    private RiskProfile perfilRisco;
    private Integer scoreRisco;
    private String objetivos;
    private String recomendacaoGeral;
    private LocalDateTime geradoEm;

    public DiagnosticoResponse(Long id, Long clienteId, RiskProfile perfilRisco, Integer scoreRisco, String objetivos, String recomendacaoGeral, LocalDateTime geradoEm) {
        this.id = id;
        this.clienteId = clienteId;
        this.perfilRisco = perfilRisco;
        this.scoreRisco = scoreRisco;
        this.objetivos = objetivos;
        this.recomendacaoGeral = recomendacaoGeral;
        this.geradoEm = geradoEm;
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public RiskProfile getPerfilRisco() {
        return perfilRisco;
    }

    public Integer getScoreRisco() {
        return scoreRisco;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public String getRecomendacaoGeral() {
        return recomendacaoGeral;
    }

    public LocalDateTime getGeradoEm() {
        return geradoEm;
    }
}
