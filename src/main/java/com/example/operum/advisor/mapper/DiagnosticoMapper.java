package com.example.operum.advisor.mapper;

import com.example.operum.advisor.domain.entity.Diagnostico;
import com.example.operum.advisor.dto.response.DiagnosticoResponse;

public final class DiagnosticoMapper {

    private DiagnosticoMapper() {
    }

    public static DiagnosticoResponse toResponse(Diagnostico diagnostico) {
        return new DiagnosticoResponse(
            diagnostico.getId(),
            diagnostico.getCliente().getId(),
            diagnostico.getPerfilRisco(),
            diagnostico.getScoreRisco(),
            diagnostico.getObjetivos(),
            diagnostico.getRecomendacaoGeral(),
            diagnostico.getGeradoEm()
        );
    }
}
