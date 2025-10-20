package com.example.operum.advisor.service.impl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.domain.entity.Diagnostico;
import com.example.operum.advisor.domain.enums.RiskProfile;
import com.example.operum.advisor.dto.request.DiagnosticoRequest;
import com.example.operum.advisor.dto.response.DiagnosticoResponse;
import com.example.operum.advisor.exception.ResourceNotFoundException;
import com.example.operum.advisor.repository.DiagnosticoRepository;
import com.example.operum.advisor.service.ClienteService;

/**
 * Testes unitários para DiagnosticoServiceImpl.
 *
 * Valida: - Cálculo de perfil de risco baseado em score e perfil do cliente -
 * Geração de recomendações personalizadas - Combinação de objetivos -
 * Validações de negócio - Tratamento de exceções
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DiagnosticoServiceImpl - Testes Unitários")
class DiagnosticoServiceImplTest {

    @Mock
    private DiagnosticoRepository diagnosticoRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private DiagnosticoServiceImpl diagnosticoService;

    private Cliente cliente;
    private DiagnosticoRequest request;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        ReflectionTestUtils.setField(cliente, "id", 1L);
        cliente.setNome("João Silva");
        cliente.setPerfilRisco(RiskProfile.CONSERVADOR);
        cliente.setObjetivos("Aposentadoria");

        request = new DiagnosticoRequest();
        request.setClienteId(1L);
        request.setScoreRisco(50);
        request.setObjetivosComplementares("Educação dos filhos");
    }

    @Test
    @DisplayName("Deve criar novo diagnóstico quando não existir")
    void deveCriarNovoDiagnostico() {
        // Arrange
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.empty());

        Diagnostico diagnosticoSalvo = new Diagnostico();
        ReflectionTestUtils.setField(diagnosticoSalvo, "id", 1L);
        diagnosticoSalvo.setCliente(cliente);
        diagnosticoSalvo.setPerfilRisco(RiskProfile.MODERADO);
        diagnosticoSalvo.setScoreRisco(50);

        when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnosticoSalvo);

        // Act
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        verify(diagnosticoRepository).save(any(Diagnostico.class));
        verify(clienteService).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve atualizar diagnóstico existente")
    void deveAtualizarDiagnosticoExistente() {
        // Arrange
        Diagnostico diagnosticoExistente = new Diagnostico();
        ReflectionTestUtils.setField(diagnosticoExistente, "id", 1L);
        diagnosticoExistente.setCliente(cliente);
        diagnosticoExistente.setPerfilRisco(RiskProfile.CONSERVADOR);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.of(diagnosticoExistente));
        when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnosticoExistente);

        // Act
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);

        // Assert
        assertThat(response).isNotNull();
        verify(diagnosticoRepository).findByClienteId(1L);
        verify(diagnosticoRepository).save(diagnosticoExistente);
    }

    @Test
    @DisplayName("Deve calcular perfil CONSERVADOR para score baixo (≤30)")
    void deveCalcularPerfilConservadorParaScoreBaixo() {
        // Arrange
        request.setScoreRisco(25);
        cliente.setPerfilRisco(RiskProfile.CONSERVADOR);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.empty());

        Diagnostico diagnosticoSalvo = new Diagnostico();
        ReflectionTestUtils.setField(diagnosticoSalvo, "id", 1L);
        diagnosticoSalvo.setPerfilRisco(RiskProfile.CONSERVADOR);

        when(diagnosticoRepository.save(any(Diagnostico.class))).thenAnswer(invocation -> {
            Diagnostico diag = invocation.getArgument(0);
            ReflectionTestUtils.setField(diagnosticoSalvo, "id", 1L);
            diagnosticoSalvo.setCliente(diag.getCliente());
            diagnosticoSalvo.setPerfilRisco(diag.getPerfilRisco());
            diagnosticoSalvo.setScoreRisco(diag.getScoreRisco());
            diagnosticoSalvo.setObjetivos(diag.getObjetivos());
            diagnosticoSalvo.setRecomendacaoGeral(diag.getRecomendacaoGeral());
            return diagnosticoSalvo;
        });

        // Act
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getPerfilRisco()).isEqualTo(RiskProfile.CONSERVADOR);
        assertThat(response.getRecomendacaoGeral()).contains("renda fixa");
    }

    @Test
    @DisplayName("Deve calcular perfil MODERADO para score médio (31-60)")
    void deveCalcularPerfilModeradoParaScoreMedio() {
        // Arrange
        request.setScoreRisco(45);
        cliente.setPerfilRisco(RiskProfile.CONSERVADOR);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.empty());

        Diagnostico diagnosticoSalvo = new Diagnostico();
        ReflectionTestUtils.setField(diagnosticoSalvo, "id", 1L);

        when(diagnosticoRepository.save(any(Diagnostico.class))).thenAnswer(invocation -> {
            Diagnostico diag = invocation.getArgument(0);
            diagnosticoSalvo.setCliente(diag.getCliente());
            diagnosticoSalvo.setPerfilRisco(diag.getPerfilRisco());
            diagnosticoSalvo.setScoreRisco(diag.getScoreRisco());
            diagnosticoSalvo.setObjetivos(diag.getObjetivos());
            diagnosticoSalvo.setRecomendacaoGeral(diag.getRecomendacaoGeral());
            return diagnosticoSalvo;
        });

        // Act
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getPerfilRisco()).isEqualTo(RiskProfile.MODERADO);
        assertThat(response.getRecomendacaoGeral()).contains("fundos");
    }

    @Test
    @DisplayName("Deve calcular perfil ARROJADO para score alto (>60)")
    void deveCalcularPerfilArrojadoParaScoreAlto() {
        // Arrange
        request.setScoreRisco(85);
        cliente.setPerfilRisco(RiskProfile.MODERADO);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.empty());

        Diagnostico diagnosticoSalvo = new Diagnostico();
        ReflectionTestUtils.setField(diagnosticoSalvo, "id", 1L);

        when(diagnosticoRepository.save(any(Diagnostico.class))).thenAnswer(invocation -> {
            Diagnostico diag = invocation.getArgument(0);
            diagnosticoSalvo.setCliente(diag.getCliente());
            diagnosticoSalvo.setPerfilRisco(diag.getPerfilRisco());
            diagnosticoSalvo.setScoreRisco(diag.getScoreRisco());
            diagnosticoSalvo.setObjetivos(diag.getObjetivos());
            diagnosticoSalvo.setRecomendacaoGeral(diag.getRecomendacaoGeral());
            return diagnosticoSalvo;
        });

        // Act
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getPerfilRisco()).isEqualTo(RiskProfile.ARROJADO);
        assertThat(response.getRecomendacaoGeral()).contains("renda variavel");
    }

    @Test
    @DisplayName("Deve usar perfil do cliente se for maior que score")
    void deveUsarPerfilClienteSeForMaior() {
        // Arrange
        request.setScoreRisco(20); // Score baixo = CONSERVADOR
        cliente.setPerfilRisco(RiskProfile.ARROJADO); // Perfil alto

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.empty());

        Diagnostico diagnosticoSalvo = new Diagnostico();
        ReflectionTestUtils.setField(diagnosticoSalvo, "id", 1L);

        when(diagnosticoRepository.save(any(Diagnostico.class))).thenAnswer(invocation -> {
            Diagnostico diag = invocation.getArgument(0);
            diagnosticoSalvo.setCliente(diag.getCliente());
            diagnosticoSalvo.setPerfilRisco(diag.getPerfilRisco());
            diagnosticoSalvo.setScoreRisco(diag.getScoreRisco());
            diagnosticoSalvo.setObjetivos(diag.getObjetivos());
            diagnosticoSalvo.setRecomendacaoGeral(diag.getRecomendacaoGeral());
            return diagnosticoSalvo;
        });

        // Act
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);

        // Assert
        assertThat(response.getPerfilRisco()).isEqualTo(RiskProfile.ARROJADO);
    }

    @Test
    @DisplayName("Deve combinar objetivos do cliente com complementares")
    void deveCombinarObjetivos() {
        // Arrange
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.empty());

        Diagnostico diagnosticoSalvo = new Diagnostico();
        ReflectionTestUtils.setField(diagnosticoSalvo, "id", 1L);

        when(diagnosticoRepository.save(any(Diagnostico.class))).thenAnswer(invocation -> {
            Diagnostico diag = invocation.getArgument(0);
            diagnosticoSalvo.setCliente(diag.getCliente());
            diagnosticoSalvo.setObjetivos(diag.getObjetivos());
            diagnosticoSalvo.setPerfilRisco(diag.getPerfilRisco());
            diagnosticoSalvo.setScoreRisco(diag.getScoreRisco());
            diagnosticoSalvo.setRecomendacaoGeral(diag.getRecomendacaoGeral());
            return diagnosticoSalvo;
        });

        // Act
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);

        // Assert
        assertThat(response.getObjetivos()).contains("Aposentadoria");
        assertThat(response.getObjetivos()).contains("Educação dos filhos");
        assertThat(response.getObjetivos()).contains(";");
    }

    @Test
    @DisplayName("Deve usar apenas objetivos complementares quando cliente não tem")
    void deveUsarApenasObjetivosComplementares() {
        // Arrange
        cliente.setObjetivos(null);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.empty());

        Diagnostico diagnosticoSalvo = new Diagnostico();
        ReflectionTestUtils.setField(diagnosticoSalvo, "id", 1L);

        when(diagnosticoRepository.save(any(Diagnostico.class))).thenAnswer(invocation -> {
            Diagnostico diag = invocation.getArgument(0);
            diagnosticoSalvo.setCliente(diag.getCliente());
            diagnosticoSalvo.setObjetivos(diag.getObjetivos());
            diagnosticoSalvo.setPerfilRisco(diag.getPerfilRisco());
            diagnosticoSalvo.setScoreRisco(diag.getScoreRisco());
            diagnosticoSalvo.setRecomendacaoGeral(diag.getRecomendacaoGeral());
            return diagnosticoSalvo;
        });

        // Act
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);

        // Assert
        assertThat(response.getObjetivos()).isEqualTo("Educação dos filhos");
    }

    @Test
    @DisplayName("Deve buscar diagnóstico por cliente com sucesso")
    void deveBuscarDiagnosticoPorCliente() {
        // Arrange
        Diagnostico diagnostico = new Diagnostico();
        ReflectionTestUtils.setField(diagnostico, "id", 1L);
        diagnostico.setCliente(cliente);
        diagnostico.setPerfilRisco(RiskProfile.MODERADO);

        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.of(diagnostico));

        // Act
        Diagnostico resultado = diagnosticoService.buscarPorCliente(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getPerfilRisco()).isEqualTo(RiskProfile.MODERADO);
        verify(diagnosticoRepository).findByClienteId(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando diagnóstico não encontrado")
    void deveLancarExcecaoQuandoDiagnosticoNaoEncontrado() {
        // Arrange
        when(diagnosticoRepository.findByClienteId(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> diagnosticoService.buscarPorCliente(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Diagnostico nao encontrado");

        verify(diagnosticoRepository).findByClienteId(999L);
    }
}
