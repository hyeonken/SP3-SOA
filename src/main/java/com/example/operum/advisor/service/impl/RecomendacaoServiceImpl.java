package com.example.operum.advisor.service.impl;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.domain.entity.Diagnostico;
import com.example.operum.advisor.domain.entity.Recomendacao;
import com.example.operum.advisor.domain.enums.RiskProfile;
import com.example.operum.advisor.dto.request.RecomendacaoRequest;
import com.example.operum.advisor.dto.response.RecomendacaoResponse;
import com.example.operum.advisor.exception.BusinessException;
import com.example.operum.advisor.mapper.RecomendacaoMapper;
import com.example.operum.advisor.repository.CarteiraRepository;
import com.example.operum.advisor.repository.RecomendacaoRepository;
import com.example.operum.advisor.service.ClienteService;
import com.example.operum.advisor.service.DiagnosticoService;
import com.example.operum.advisor.service.RecomendacaoService;
import com.example.operum.advisor.strategy.AgressivaStrategy;
import com.example.operum.advisor.strategy.CarteiraSelectionStrategy;
import com.example.operum.advisor.strategy.ConservadoraStrategy;
import com.example.operum.advisor.strategy.ModeradaStrategy;

/**
 * Implementação do serviço de recomendações de carteira.
 *
 * Utiliza o padrão Strategy para selecionar dinamicamente a estratégia de
 * composição de carteira baseada no perfil de risco do cliente.
 *
 * Demonstra POLIMORFISMO e DESPACHO DINÂMICO através do Map de estratégias,
 * permitindo extensão sem modificação (Open/Closed Principle).
 */
@Service
public class RecomendacaoServiceImpl implements RecomendacaoService {

    private final RecomendacaoRepository recomendacaoRepository;
    private final ClienteService clienteService;
    private final DiagnosticoService diagnosticoService;
    private final CarteiraRepository carteiraRepository;

    /**
     * Map de estratégias indexado por perfil de risco. Demonstra POLIMORFISMO:
     * diferentes objetos (ConservadoraStrategy, ModeradaStrategy, etc)
     * respondem à mesma interface CarteiraSelectionStrategy.
     *
     * Demonstra DESPACHO DINÂMICO: a estratégia correta é selecionada em tempo
     * de execução baseada no perfil do cliente.
     */
    private final Map<RiskProfile, CarteiraSelectionStrategy> strategies;

    public RecomendacaoServiceImpl(
            RecomendacaoRepository recomendacaoRepository,
            ClienteService clienteService,
            DiagnosticoService diagnosticoService,
            CarteiraRepository carteiraRepository,
            ConservadoraStrategy conservadoraStrategy,
            ModeradaStrategy moderadaStrategy,
            AgressivaStrategy agressivaStrategy) {
        this.recomendacaoRepository = recomendacaoRepository;
        this.clienteService = clienteService;
        this.diagnosticoService = diagnosticoService;
        this.carteiraRepository = carteiraRepository;

        // Inicializa o mapa de estratégias - POLIMORFISMO
        this.strategies = Map.of(
                RiskProfile.CONSERVADOR, conservadoraStrategy,
                RiskProfile.MODERADO, moderadaStrategy,
                RiskProfile.ARROJADO, agressivaStrategy
        );
    }

    @Override
    @Transactional
    public RecomendacaoResponse gerar(RecomendacaoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId());
        Diagnostico diagnostico = diagnosticoService.buscarPorCliente(cliente.getId());

        // DESPACHO DINÂMICO: seleciona a estratégia em tempo de execução
        CarteiraSelectionStrategy strategy = strategies.get(diagnostico.getPerfilRisco());
        if (strategy == null) {
            throw new BusinessException("Estrategia nao encontrada para o perfil: " + diagnostico.getPerfilRisco());
        }

        // POLIMORFISMO: chama selectCarteira() que terá comportamento diferente
        // dependendo da implementação concreta (Conservadora, Moderada ou Agressiva)
        Carteira carteiraGerada = strategy.selectCarteira(diagnostico.getPerfilRisco(), request.getValorDisponivel());

        // Persiste a carteira gerada pela estratégia
        Carteira carteiraSalva = carteiraRepository.save(carteiraGerada);

        Recomendacao recomendacao = new Recomendacao();
        recomendacao.setCliente(cliente);
        recomendacao.setCarteira(carteiraSalva);
        recomendacao.setJustificativa(gerarJustificativa(carteiraSalva, diagnostico, request, strategy));

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

    private String gerarJustificativa(Carteira carteira, Diagnostico diagnostico,
            RecomendacaoRequest request, CarteiraSelectionStrategy strategy) {
        StringBuilder builder = new StringBuilder();
        builder.append("Perfil identificado: ").append(diagnostico.getPerfilRisco());
        builder.append(" | Estrategia aplicada: ").append(strategy.getNomeEstrategia());

        if (request.getValorDisponivel() != null) {
            builder.append(" | Valor disponivel informado: R$ ")
                    .append(request.getValorDisponivel().setScale(2, RoundingMode.HALF_UP));
        }
        if (request.getObjetivo() != null && !request.getObjetivo().isBlank()) {
            builder.append(" | Objetivo: ").append(request.getObjetivo());
        }

        builder.append(" | Carteira sugerida: ").append(carteira.getNome());
        builder.append(" | Retorno esperado: ").append(carteira.getRetornoEsperado()).append("%");
        builder.append(" | Risco estimado: ").append(carteira.getRiscoEstimado());

        return builder.toString();
    }
}
