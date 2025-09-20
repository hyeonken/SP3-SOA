package com.example.operum.advisor.service.impl;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.domain.entity.Diagnostico;
import com.example.operum.advisor.domain.entity.Recomendacao;
import com.example.operum.advisor.dto.request.RecomendacaoRequest;
import com.example.operum.advisor.dto.response.RecomendacaoResponse;
import com.example.operum.advisor.exception.BusinessException;
import com.example.operum.advisor.mapper.RecomendacaoMapper;
import com.example.operum.advisor.repository.CarteiraRepository;
import com.example.operum.advisor.repository.RecomendacaoRepository;
import com.example.operum.advisor.service.ClienteService;
import com.example.operum.advisor.service.DiagnosticoService;
import com.example.operum.advisor.service.RecomendacaoService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecomendacaoServiceImpl implements RecomendacaoService {

    private final RecomendacaoRepository recomendacaoRepository;
    private final ClienteService clienteService;
    private final DiagnosticoService diagnosticoService;
    private final CarteiraRepository carteiraRepository;

    public RecomendacaoServiceImpl(RecomendacaoRepository recomendacaoRepository, ClienteService clienteService,
                                   DiagnosticoService diagnosticoService, CarteiraRepository carteiraRepository) {
        this.recomendacaoRepository = recomendacaoRepository;
        this.clienteService = clienteService;
        this.diagnosticoService = diagnosticoService;
        this.carteiraRepository = carteiraRepository;
    }

    @Override
    @Transactional
    public RecomendacaoResponse gerar(RecomendacaoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId());
        Diagnostico diagnostico = diagnosticoService.buscarPorCliente(cliente.getId());

        List<Carteira> carteiras = carteiraRepository.findByPerfilRisco(diagnostico.getPerfilRisco());
        if (carteiras.isEmpty()) {
            throw new BusinessException("Nao ha carteiras cadastradas para o perfil do cliente");
        }

        Carteira selecionada = selecionarCarteira(carteiras, request.getValorDisponivel());
        Recomendacao recomendacao = new Recomendacao();
        recomendacao.setCliente(cliente);
        recomendacao.setCarteira(selecionada);
        recomendacao.setJustificativa(gerarJustificativa(selecionada, diagnostico, request));

        Recomendacao salva = recomendacaoRepository.save(recomendacao);
        return RecomendacaoMapper.toResponse(salva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacaoResponse> buscarPorCliente(Long clienteId) {
        return recomendacaoRepository.findByClienteId(clienteId).stream()
            .map(RecomendacaoMapper::toResponse)
            .toList();
    }

    private Carteira selecionarCarteira(List<Carteira> carteiras, BigDecimal valorDisponivel) {
        return carteiras.stream()
            .sorted(Comparator.comparing(Carteira::getRetornoEsperado, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
            .findFirst()
            .orElseThrow(() -> new BusinessException("Nao foi possivel selecionar uma carteira"));
    }

    private String gerarJustificativa(Carteira carteira, Diagnostico diagnostico, RecomendacaoRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("Perfil identificado: ").append(diagnostico.getPerfilRisco());
        if (request.getValorDisponivel() != null) {
            builder.append(" | Valor disponivel informado: R$ ")
                .append(request.getValorDisponivel().setScale(2, RoundingMode.HALF_UP));
        }
        if (request.getObjetivo() != null && !request.getObjetivo().isBlank()) {
            builder.append(" | Objetivo: ").append(request.getObjetivo());
        }
        builder.append(" | Carteira sugerida: ").append(carteira.getNome());
        return builder.toString();
    }
}
