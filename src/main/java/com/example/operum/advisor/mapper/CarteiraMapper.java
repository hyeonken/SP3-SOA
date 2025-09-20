package com.example.operum.advisor.mapper;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.entity.ItemCarteira;
import com.example.operum.advisor.dto.response.CarteiraResponse;
import com.example.operum.advisor.dto.response.ItemCarteiraResponse;
import java.util.List;
import java.util.stream.Collectors;

public final class CarteiraMapper {

    private CarteiraMapper() {
    }

    public static CarteiraResponse toResponse(Carteira carteira) {
        List<ItemCarteiraResponse> itens = carteira.getItens().stream()
            .map(CarteiraMapper::toItemResponse)
            .collect(Collectors.toList());
        return new CarteiraResponse(
            carteira.getId(),
            carteira.getNome(),
            carteira.getDescricao(),
            carteira.getPerfilRisco(),
            carteira.getRetornoEsperado(),
            carteira.getRiscoEstimado(),
            itens
        );
    }

    private static ItemCarteiraResponse toItemResponse(ItemCarteira item) {
        return new ItemCarteiraResponse(
            item.getAtivo().getCodigo(),
            item.getAtivo().getNome(),
            item.getPercentual()
        );
    }
}
