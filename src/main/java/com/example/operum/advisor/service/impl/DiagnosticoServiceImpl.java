package com.example.operum.advisor.service.impl;

import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.domain.entity.Diagnostico;
import com.example.operum.advisor.domain.enums.RiskProfile;
import com.example.operum.advisor.dto.request.DiagnosticoRequest;
import com.example.operum.advisor.dto.response.DiagnosticoResponse;
import com.example.operum.advisor.exception.ResourceNotFoundException;
import com.example.operum.advisor.mapper.DiagnosticoMapper;
import com.example.operum.advisor.repository.DiagnosticoRepository;
import com.example.operum.advisor.service.ClienteService;
import com.example.operum.advisor.service.DiagnosticoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final ClienteService clienteService;

    public DiagnosticoServiceImpl(DiagnosticoRepository diagnosticoRepository, ClienteService clienteService) {
        this.diagnosticoRepository = diagnosticoRepository;
        this.clienteService = clienteService;
    }

    @Override
    @Transactional
    public DiagnosticoResponse gerarDiagnostico(DiagnosticoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId());
        RiskProfile perfilCalculado = calcularPerfil(request.getScoreRisco(), cliente.getPerfilRisco());
        String objetivos = combinarObjetivos(cliente.getObjetivos(), request.getObjetivosComplementares());
        String recomendacao = gerarRecomendacaoTexto(perfilCalculado);

        Diagnostico diagnostico = diagnosticoRepository.findByClienteId(cliente.getId())
            .orElseGet(Diagnostico::new);

        diagnostico.setCliente(cliente);
        diagnostico.setPerfilRisco(perfilCalculado);
        diagnostico.setScoreRisco(request.getScoreRisco());
        diagnostico.setObjetivos(objetivos);
        diagnostico.setRecomendacaoGeral(recomendacao);

        Diagnostico salvo = diagnosticoRepository.save(diagnostico);
        return DiagnosticoMapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public Diagnostico buscarPorCliente(Long clienteId) {
        return diagnosticoRepository.findByClienteId(clienteId)
            .orElseThrow(() -> new ResourceNotFoundException("Diagnostico nao encontrado para o cliente"));
    }

    private RiskProfile calcularPerfil(int score, RiskProfile perfilCliente) {
        int scoreNivel = score <= 30 ? 0 : score <= 60 ? 1 : 2;
        int perfilNivel = perfilCliente != null ? perfilCliente.ordinal() : 0;
        int nivelFinal = Math.max(scoreNivel, perfilNivel);
        return RiskProfile.values()[Math.min(nivelFinal, RiskProfile.values().length - 1)];
    }

    private String combinarObjetivos(String objetivosCliente, String objetivosComplementares) {
        if (objetivosCliente == null || objetivosCliente.isBlank()) {
            return objetivosComplementares;
        }
        if (objetivosComplementares == null || objetivosComplementares.isBlank()) {
            return objetivosCliente;
        }
        return objetivosCliente + "; " + objetivosComplementares;
    }

    private String gerarRecomendacaoTexto(RiskProfile perfil) {
        return switch (perfil) {
            case CONSERVADOR -> "Recomenda-se foco em produtos de renda fixa com liquidez e baixa volatilidade.";
            case MODERADO -> "Combinar renda fixa com fundos e ETFs pode equilibrar riscos e retornos.";
            case ARROJADO -> "Avalie maior participacao em renda variavel e ativos internacionais para maximizar retorno.";
        };
    }
}
