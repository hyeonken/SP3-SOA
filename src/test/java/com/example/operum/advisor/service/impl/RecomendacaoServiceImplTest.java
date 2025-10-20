package com.example.operum.advisor.service.impl;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.Mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.operum.advisor.domain.entity.Carteira;
import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.domain.entity.Diagnostico;
import com.example.operum.advisor.domain.entity.Recomendacao;
import com.example.operum.advisor.domain.enums.RiskProfile;
import com.example.operum.advisor.dto.request.RecomendacaoRequest;
import com.example.operum.advisor.dto.response.RecomendacaoResponse;
import com.example.operum.advisor.repository.CarteiraRepository;
import com.example.operum.advisor.repository.RecomendacaoRepository;
import com.example.operum.advisor.service.ClienteService;
import com.example.operum.advisor.service.DiagnosticoService;
import com.example.operum.advisor.strategy.AgressivaStrategy;
import com.example.operum.advisor.strategy.ConservadoraStrategy;
import com.example.operum.advisor.strategy.ModeradaStrategy;

/**
 * Testes unitários para RecomendacaoServiceImpl.
 *
 * Foco principal: validar integração com Strategy Pattern - Polimorfismo -
 * Despacho dinâmico - Seleção de estratégia baseada em perfil de risco
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("RecomendacaoServiceImpl - Testes com Strategy Pattern")
class RecomendacaoServiceImplTest {

    @Mock
    private RecomendacaoRepository recomendacaoRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private DiagnosticoService diagnosticoService;

    @Mock
    private CarteiraRepository carteiraRepository;

    private RecomendacaoServiceImpl recomendacaoService;

    private Cliente cliente;
    private Diagnostico diagnostico;
    private RecomendacaoRequest request;
    private ConservadoraStrategy conservadoraStrategy;
    private ModeradaStrategy moderadaStrategy;
    private AgressivaStrategy agressivaStrategy;

    @BeforeEach
    void setUp() {
        // Estratégias reais (não mocks) para testar polimorfismo
        conservadoraStrategy = new ConservadoraStrategy();
        moderadaStrategy = new ModeradaStrategy();
        agressivaStrategy = new AgressivaStrategy();

        // Criação manual do service com estratégias reais
        recomendacaoService = new RecomendacaoServiceImpl(
                recomendacaoRepository,
                clienteService,
                diagnosticoService,
                carteiraRepository,
                conservadoraStrategy,
                moderadaStrategy,
                agressivaStrategy
        );

        // Setup cliente
        cliente = new Cliente();
        ReflectionTestUtils.setField(cliente, "id", 1L);
        cliente.setNome("Maria Silva");
        cliente.setPerfilRisco(RiskProfile.MODERADO);

        // Setup request
        request = new RecomendacaoRequest();
        request.setClienteId(1L);
        request.setValorDisponivel(new BigDecimal("100000.00"));
        request.setObjetivo("Crescimento patrimonial");

        // Setup diagnostico
        diagnostico = new Diagnostico();
        ReflectionTestUtils.setField(diagnostico, "id", 1L);
        diagnostico.setCliente(cliente);
        diagnostico.setPerfilRisco(RiskProfile.MODERADO);
    }

    @Test
    @DisplayName("Deve gerar recomendação com estratégia CONSERVADORA - Strategy Pattern")
    void deveGerarRecomendacaoConservadora() {
        // Arrange
        diagnostico.setPerfilRisco(RiskProfile.CONSERVADOR);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoService.buscarPorCliente(1L)).thenReturn(diagnostico);

        Carteira carteiraSalva = new Carteira();
        ReflectionTestUtils.setField(carteiraSalva, "id", 10L);
        carteiraSalva.setNome("Carteira Conservadora - Preservação de Capital");
        carteiraSalva.setPerfilRisco(RiskProfile.CONSERVADOR);
        carteiraSalva.setRetornoEsperado(new BigDecimal("10.5"));

        when(carteiraRepository.save(any(Carteira.class))).thenReturn(carteiraSalva);

        Recomendacao recomendacaoSalva = new Recomendacao();
        ReflectionTestUtils.setField(recomendacaoSalva, "id", 1L);
        recomendacaoSalva.setCliente(cliente);
        recomendacaoSalva.setCarteira(carteiraSalva);

        when(recomendacaoRepository.save(any(Recomendacao.class))).thenReturn(recomendacaoSalva);

        // Act
        RecomendacaoResponse response = recomendacaoService.gerar(request);

        // Assert - Valida que estratégia CONSERVADORA foi usada
        assertThat(response).isNotNull();
        verify(carteiraRepository).save(argThat(carteira
                -> carteira.getNome().contains("Conservadora")
                && carteira.getRetornoEsperado().compareTo(new BigDecimal("10.5")) == 0
        ));
    }

    @Test
    @DisplayName("Deve gerar recomendação com estratégia MODERADA - Strategy Pattern")
    void deveGerarRecomendacaoModerada() {
        // Arrange
        diagnostico.setPerfilRisco(RiskProfile.MODERADO);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoService.buscarPorCliente(1L)).thenReturn(diagnostico);

        Carteira carteiraSalva = new Carteira();
        ReflectionTestUtils.setField(carteiraSalva, "id", 10L);
        carteiraSalva.setNome("Carteira Moderada - Crescimento Equilibrado");
        carteiraSalva.setPerfilRisco(RiskProfile.MODERADO);
        carteiraSalva.setRetornoEsperado(new BigDecimal("16.0"));

        when(carteiraRepository.save(any(Carteira.class))).thenReturn(carteiraSalva);

        Recomendacao recomendacaoSalva = new Recomendacao();
        ReflectionTestUtils.setField(recomendacaoSalva, "id", 1L);
        recomendacaoSalva.setCliente(cliente);
        recomendacaoSalva.setCarteira(carteiraSalva);

        when(recomendacaoRepository.save(any(Recomendacao.class))).thenReturn(recomendacaoSalva);

        // Act
        RecomendacaoResponse response = recomendacaoService.gerar(request);

        // Assert - Valida que estratégia MODERADA foi usada
        assertThat(response).isNotNull();
        verify(carteiraRepository).save(argThat(carteira
                -> carteira.getNome().contains("Moderada")
                && carteira.getRetornoEsperado().compareTo(new BigDecimal("16.0")) == 0
        ));
    }

    @Test
    @DisplayName("Deve gerar recomendação com estratégia AGRESSIVA - Strategy Pattern")
    void deveGerarRecomendacaoAgressiva() {
        // Arrange
        diagnostico.setPerfilRisco(RiskProfile.ARROJADO);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoService.buscarPorCliente(1L)).thenReturn(diagnostico);

        Carteira carteiraSalva = new Carteira();
        ReflectionTestUtils.setField(carteiraSalva, "id", 10L);
        carteiraSalva.setNome("Carteira Agressiva - Crescimento Acelerado");
        carteiraSalva.setPerfilRisco(RiskProfile.ARROJADO);
        carteiraSalva.setRetornoEsperado(new BigDecimal("25.0"));

        when(carteiraRepository.save(any(Carteira.class))).thenReturn(carteiraSalva);

        Recomendacao recomendacaoSalva = new Recomendacao();
        ReflectionTestUtils.setField(recomendacaoSalva, "id", 1L);
        recomendacaoSalva.setCliente(cliente);
        recomendacaoSalva.setCarteira(carteiraSalva);

        when(recomendacaoRepository.save(any(Recomendacao.class))).thenReturn(recomendacaoSalva);

        // Act
        RecomendacaoResponse response = recomendacaoService.gerar(request);

        // Assert - Valida que estratégia AGRESSIVA foi usada
        assertThat(response).isNotNull();
        verify(carteiraRepository).save(argThat(carteira
                -> carteira.getNome().contains("Agressiva")
                && carteira.getRetornoEsperado().compareTo(new BigDecimal("25.0")) == 0
        ));
    }

    @Test
    @DisplayName("Deve demonstrar DESPACHO DINÂMICO - diferentes estratégias em runtime")
    void deveDemonstrarDespachoDinamico() {
        // Arrange - Testa os 3 perfis em sequência
        RiskProfile[] perfis = {RiskProfile.CONSERVADOR, RiskProfile.MODERADO, RiskProfile.ARROJADO};

        for (RiskProfile perfil : perfis) {
            diagnostico.setPerfilRisco(perfil);

            when(clienteService.buscarPorId(1L)).thenReturn(cliente);
            when(diagnosticoService.buscarPorCliente(1L)).thenReturn(diagnostico);

            Carteira carteiraSalva = new Carteira();
            ReflectionTestUtils.setField(carteiraSalva, "id", 10L);
            carteiraSalva.setPerfilRisco(perfil);

            when(carteiraRepository.save(any(Carteira.class))).thenReturn(carteiraSalva);

            Recomendacao recomendacaoSalva = new Recomendacao();
            ReflectionTestUtils.setField(recomendacaoSalva, "id", 1L);
            recomendacaoSalva.setCliente(cliente);
            recomendacaoSalva.setCarteira(carteiraSalva);

            when(recomendacaoRepository.save(any(Recomendacao.class))).thenReturn(recomendacaoSalva);

            // Act - Despacho dinâmico
            RecomendacaoResponse response = recomendacaoService.gerar(request);

            // Assert - Perfil correto foi usado
            assertThat(response).isNotNull();
        }

        // Valida que carteira foi salva 3 vezes (uma para cada perfil)
        verify(carteiraRepository, times(3)).save(any(Carteira.class));
    }

    @Test
    @DisplayName("Deve validar carteira é persistida antes da recomendação")
    void deveValidarCarteiraPersistida() {
        // Arrange
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoService.buscarPorCliente(1L)).thenReturn(diagnostico);

        Carteira carteiraSalva = new Carteira();
        ReflectionTestUtils.setField(carteiraSalva, "id", 10L);

        when(carteiraRepository.save(any(Carteira.class))).thenReturn(carteiraSalva);

        Recomendacao recomendacaoSalva = new Recomendacao();
        ReflectionTestUtils.setField(recomendacaoSalva, "id", 1L);
        recomendacaoSalva.setCliente(cliente);
        recomendacaoSalva.setCarteira(carteiraSalva);

        when(recomendacaoRepository.save(any(Recomendacao.class))).thenReturn(recomendacaoSalva);

        // Act
        recomendacaoService.gerar(request);

        // Assert - Carteira salva ANTES da recomendação
        var inOrder = inOrder(carteiraRepository, recomendacaoRepository);
        inOrder.verify(carteiraRepository).save(any(Carteira.class));
        inOrder.verify(recomendacaoRepository).save(any(Recomendacao.class));
    }

    @Test
    @DisplayName("Deve incluir nome da estratégia na justificativa")
    void deveIncluirNomeEstrategiaNaJustificativa() {
        // Arrange
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoService.buscarPorCliente(1L)).thenReturn(diagnostico);

        Carteira carteiraSalva = new Carteira();
        ReflectionTestUtils.setField(carteiraSalva, "id", 10L);
        carteiraSalva.setNome("Carteira Moderada");
        carteiraSalva.setRetornoEsperado(new BigDecimal("16.0"));
        carteiraSalva.setRiscoEstimado(new BigDecimal("5.5"));

        when(carteiraRepository.save(any(Carteira.class))).thenReturn(carteiraSalva);

        Recomendacao recomendacaoSalva = new Recomendacao();
        ReflectionTestUtils.setField(recomendacaoSalva, "id", 1L);
        recomendacaoSalva.setCliente(cliente);
        recomendacaoSalva.setCarteira(carteiraSalva);

        when(recomendacaoRepository.save(any(Recomendacao.class))).thenAnswer(invocation -> {
            Recomendacao rec = invocation.getArgument(0);
            recomendacaoSalva.setJustificativa(rec.getJustificativa());
            return recomendacaoSalva;
        });

        // Act
        RecomendacaoResponse response = recomendacaoService.gerar(request);

        // Assert - Justificativa contém nome da estratégia
        verify(recomendacaoRepository).save(argThat(rec
                -> rec.getJustificativa().contains("Moderada")
        ));
    }

    @Test
    @DisplayName("Deve buscar recomendações por cliente")
    void deveBuscarRecomendacoesPorCliente() {
        // Arrange
        Carteira carteira = new Carteira();
        ReflectionTestUtils.setField(carteira, "id", 10L);

        Recomendacao rec1 = new Recomendacao();
        ReflectionTestUtils.setField(rec1, "id", 1L);
        rec1.setCliente(cliente);
        rec1.setCarteira(carteira);

        Recomendacao rec2 = new Recomendacao();
        ReflectionTestUtils.setField(rec2, "id", 2L);
        rec2.setCliente(cliente);
        rec2.setCarteira(carteira);

        when(recomendacaoRepository.findByClienteId(1L)).thenReturn(List.of(rec1, rec2));

        // Act
        List<RecomendacaoResponse> responses = recomendacaoService.buscarPorCliente(1L);

        // Assert
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(1).getId()).isEqualTo(2L);
        verify(recomendacaoRepository).findByClienteId(1L);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando cliente não tem recomendações")
    void deveRetornarListaVaziaQuandoClienteSemRecomendacoes() {
        // Arrange
        when(recomendacaoRepository.findByClienteId(999L)).thenReturn(List.of());

        // Act
        List<RecomendacaoResponse> responses = recomendacaoService.buscarPorCliente(999L);

        // Assert
        assertThat(responses).isEmpty();
        verify(recomendacaoRepository).findByClienteId(999L);
    }

    @Test
    @DisplayName("Deve validar dados da recomendação gerada")
    void deveValidarDadosRecomendacao() {
        // Arrange
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoService.buscarPorCliente(1L)).thenReturn(diagnostico);

        Carteira carteiraSalva = new Carteira();
        ReflectionTestUtils.setField(carteiraSalva, "id", 10L);
        carteiraSalva.setNome("Carteira Moderada");

        when(carteiraRepository.save(any(Carteira.class))).thenReturn(carteiraSalva);

        Recomendacao recomendacaoSalva = new Recomendacao();
        ReflectionTestUtils.setField(recomendacaoSalva, "id", 1L);
        recomendacaoSalva.setCliente(cliente);
        recomendacaoSalva.setCarteira(carteiraSalva);
        recomendacaoSalva.setJustificativa("Teste");

        when(recomendacaoRepository.save(any(Recomendacao.class))).thenReturn(recomendacaoSalva);

        // Act
        RecomendacaoResponse response = recomendacaoService.gerar(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getClienteId()).isEqualTo(1L);
        assertThat(response.getCarteira()).isNotNull();
        assertThat(response.getCarteira().getId()).isEqualTo(10L);
        verify(recomendacaoRepository).save(argThat(rec
                -> rec.getCliente().equals(cliente)
                && rec.getCarteira().equals(carteiraSalva)
                && rec.getJustificativa() != null
        ));
    }
}
