package com.example.operum.advisor.service;

import com.example.operum.advisor.dto.request.RecomendacaoRequest;
import com.example.operum.advisor.dto.response.RecomendacaoResponse;
import java.util.List;

public interface RecomendacaoService {

    RecomendacaoResponse gerar(RecomendacaoRequest request);

    List<RecomendacaoResponse> buscarPorCliente(Long clienteId);
}
