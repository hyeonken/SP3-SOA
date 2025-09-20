package com.example.operum.advisor.service;

import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.dto.request.ClienteRequest;
import com.example.operum.advisor.dto.response.ClienteResponse;
import java.util.List;

public interface ClienteService {

    ClienteResponse criar(ClienteRequest request);

    List<ClienteResponse> listar();

    ClienteResponse buscarPorIdResponse(Long id);

    ClienteResponse atualizar(Long id, ClienteRequest request);

    void remover(Long id);

    Cliente buscarPorId(Long id);
}
