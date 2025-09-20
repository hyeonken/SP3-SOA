package com.example.operum.advisor.mapper;

import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.domain.vo.Cpf;
import com.example.operum.advisor.dto.request.ClienteRequest;
import com.example.operum.advisor.dto.response.ClienteResponse;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente toEntity(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setCpf(Cpf.of(request.getCpf()));
        cliente.setEmail(request.getEmail());
        cliente.setConsentimentoLgpd(Boolean.TRUE.equals(request.getConsentimentoLgpd()));
        cliente.setObjetivos(request.getObjetivos());
        cliente.setPerfilRisco(request.getPerfilRisco());
        return cliente;
    }

    public static ClienteResponse toResponse(Cliente entity) {
        return new ClienteResponse(
            entity.getId(),
            entity.getNome(),
            entity.getCpf() != null ? entity.getCpf().getValue() : null,
            entity.getEmail(),
            entity.getPerfilRisco(),
            entity.isConsentimentoLgpd()
        );
    }

    public static void merge(Cliente destino, ClienteRequest origem) {
        destino.setNome(origem.getNome());
        destino.setCpf(Cpf.of(origem.getCpf()));
        destino.setEmail(origem.getEmail());
        destino.setConsentimentoLgpd(Boolean.TRUE.equals(origem.getConsentimentoLgpd()));
        destino.setObjetivos(origem.getObjetivos());
        destino.setPerfilRisco(origem.getPerfilRisco());
    }
}
