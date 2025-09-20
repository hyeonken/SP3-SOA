package com.example.operum.advisor.service;

import com.example.operum.advisor.domain.entity.Diagnostico;
import com.example.operum.advisor.dto.request.DiagnosticoRequest;
import com.example.operum.advisor.dto.response.DiagnosticoResponse;

public interface DiagnosticoService {

    DiagnosticoResponse gerarDiagnostico(DiagnosticoRequest request);

    Diagnostico buscarPorCliente(Long clienteId);
}
