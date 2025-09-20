package com.example.operum.advisor.dto.response;

import java.time.LocalDateTime;

public class RecomendacaoResponse {

    private Long id;
    private Long clienteId;
    private CarteiraResponse carteira;
    private String justificativa;
    private LocalDateTime geradoEm;

    public RecomendacaoResponse(Long id, Long clienteId, CarteiraResponse carteira, String justificativa, LocalDateTime geradoEm) {
        this.id = id;
        this.clienteId = clienteId;
        this.carteira = carteira;
        this.justificativa = justificativa;
        this.geradoEm = geradoEm;
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public CarteiraResponse getCarteira() {
        return carteira;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public LocalDateTime getGeradoEm() {
        return geradoEm;
    }
}
