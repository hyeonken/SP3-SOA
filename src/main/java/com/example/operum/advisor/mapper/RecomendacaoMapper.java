package com.example.operum.advisor.mapper;

import com.example.operum.advisor.domain.entity.Recomendacao;
import com.example.operum.advisor.dto.response.RecomendacaoResponse;

public final class RecomendacaoMapper {

    private RecomendacaoMapper() {
    }

    public static RecomendacaoResponse toResponse(Recomendacao recomendacao) {
        return new RecomendacaoResponse(
            recomendacao.getId(),
            recomendacao.getCliente().getId(),
            CarteiraMapper.toResponse(recomendacao.getCarteira()),
            recomendacao.getJustificativa(),
            recomendacao.getGeradoEm()
        );
    }
}
