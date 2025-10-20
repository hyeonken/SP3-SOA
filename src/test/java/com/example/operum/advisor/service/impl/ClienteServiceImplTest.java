package com.example.operum.advisor.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.domain.entity.Diagnostico;
import com.example.operum.advisor.domain.entity.Recomendacao;
import com.example.operum.advisor.domain.enums.RiskProfile;
import com.example.operum.advisor.domain.vo.Cpf;
import com.example.operum.advisor.dto.request.ClienteRequest;
import com.example.operum.advisor.dto.response.ClienteResponse;
import com.example.operum.advisor.exception.BusinessException;
import com.example.operum.advisor.exception.ResourceNotFoundException;
import com.example.operum.advisor.repository.ClienteRepository;
import com.example.operum.advisor.repository.DiagnosticoRepository;
import com.example.operum.advisor.repository.RecomendacaoRepository;

/**
 * Testes unitários para ClienteServiceImpl. Valida regras de negócio,
 * validações e integração com repositórios usando mocks.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService - Testes Unitários")
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private DiagnosticoRepository diagnosticoRepository;

    @Mock
    private RecomendacaoRepository recomendacaoRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteRequest validRequest;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        validRequest = new ClienteRequest();
        validRequest.setNome("João Silva");
        validRequest.setCpf("12345678909");
        validRequest.setEmail("joao@example.com");
        validRequest.setConsentimentoLgpd(true);
        validRequest.setPerfilRisco(RiskProfile.MODERADO);

        cliente = new Cliente();
        ReflectionTestUtils.setField(cliente, "id", 1L);
        cliente.setNome("João Silva");
        ReflectionTestUtils.setField(cliente, "cpf", Cpf.of("12345678909"));
        cliente.setEmail("joao@example.com");
        cliente.setConsentimentoLgpd(true);
        cliente.setPerfilRisco(RiskProfile.MODERADO);
    }

    @Test
    @DisplayName("Deve criar cliente com sucesso quando dados válidos")
    void deveCriarClienteComSucesso() {
        // Arrange
        when(clienteRepository.existsByCpfValue(anyString())).thenReturn(false);
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteResponse response = clienteService.criar(validRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getNome()).isEqualTo("João Silva");
        assertThat(response.getEmail()).isEqualTo("joao@example.com");
        verify(clienteRepository).existsByCpfValue("12345678909");
        verify(clienteRepository).existsByEmail("joao@example.com");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando consentimento LGPD não fornecido")
    void deveLancarExcecaoQuandoConsentimentoNaoFornecido() {
        // Arrange
        validRequest.setConsentimentoLgpd(false);

        // Act & Assert
        assertThatThrownBy(() -> clienteService.criar(validRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Consentimento LGPD obrigatorio");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando consentimento LGPD é null")
    void deveLancarExcecaoQuandoConsentimentoNull() {
        // Arrange
        validRequest.setConsentimentoLgpd(null);

        // Act & Assert
        assertThatThrownBy(() -> clienteService.criar(validRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Consentimento LGPD obrigatorio");
    }

    @Test
    @DisplayName("Deve lançar exceção quando CPF duplicado")
    void deveLancarExcecaoQuandoCpfDuplicado() {
        // Arrange
        when(clienteRepository.existsByCpfValue("12345678909")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> clienteService.criar(validRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CPF ja cadastrado");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email duplicado")
    void deveLancarExcecaoQuandoEmailDuplicado() {
        // Arrange
        when(clienteRepository.existsByCpfValue(anyString())).thenReturn(false);
        when(clienteRepository.existsByEmail("joao@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> clienteService.criar(validRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email ja cadastrado");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    void deveListarTodosClientes() {
        // Arrange
        Cliente cliente2 = new Cliente();
        ReflectionTestUtils.setField(cliente2, "id", 2L);
        cliente2.setNome("Maria Santos");
        ReflectionTestUtils.setField(cliente2, "cpf", Cpf.of("98765432100"));
        cliente2.setEmail("maria@example.com");
        cliente2.setConsentimentoLgpd(true);
        cliente2.setPerfilRisco(RiskProfile.CONSERVADOR);

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente, cliente2));

        // Act
        List<ClienteResponse> responses = clienteService.listar();

        // Assert
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getNome()).isEqualTo("João Silva");
        assertThat(responses.get(1).getNome()).isEqualTo("Maria Santos");
        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há clientes")
    void deveRetornarListaVaziaQuandoNaoHaClientes() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ClienteResponse> responses = clienteService.listar();

        // Assert
        assertThat(responses).isEmpty();
        verify(clienteRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void deveBuscarClientePorIdComSucesso() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        ClienteResponse response = clienteService.buscarPorIdResponse(1L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNome()).isEqualTo("João Silva");
        verify(clienteRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não encontrado por ID")
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> clienteService.buscarPorIdResponse(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente nao encontrado");

        verify(clienteRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve atualizar cliente com sucesso")
    void deveAtualizarClienteComSucesso() {
        // Arrange
        ClienteRequest updateRequest = new ClienteRequest();
        updateRequest.setNome("João Silva Atualizado");
        updateRequest.setCpf("12345678909");
        updateRequest.setEmail("joao.novo@example.com");
        updateRequest.setConsentimentoLgpd(true);
        updateRequest.setPerfilRisco(RiskProfile.ARROJADO);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByCpfValue("12345678909")).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByEmail("joao.novo@example.com")).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteResponse response = clienteService.atualizar(1L, updateRequest);

        // Assert
        assertThat(response).isNotNull();
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar com CPF de outro cliente")
    void deveLancarExcecaoAoAtualizarComCpfDeOutroCliente() {
        // Arrange
        Cliente outroCliente = new Cliente();
        ReflectionTestUtils.setField(outroCliente, "id", 2L);
        ReflectionTestUtils.setField(outroCliente, "cpf", Cpf.of("98765432100"));

        ClienteRequest updateRequest = new ClienteRequest();
        updateRequest.setNome("João Silva");
        updateRequest.setCpf("98765432100");
        updateRequest.setEmail("joao@example.com");
        updateRequest.setConsentimentoLgpd(true);
        updateRequest.setPerfilRisco(RiskProfile.MODERADO);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByCpfValue("98765432100")).thenReturn(Optional.of(outroCliente));

        // Act & Assert
        assertThatThrownBy(() -> clienteService.atualizar(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CPF ja cadastrado");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar com email de outro cliente")
    void deveLancarExcecaoAoAtualizarComEmailDeOutroCliente() {
        // Arrange
        Cliente outroCliente = new Cliente();
        ReflectionTestUtils.setField(outroCliente, "id", 2L);
        outroCliente.setEmail("outro@example.com");

        ClienteRequest updateRequest = new ClienteRequest();
        updateRequest.setNome("João Silva");
        updateRequest.setCpf("12345678909");
        updateRequest.setEmail("outro@example.com");
        updateRequest.setConsentimentoLgpd(true);
        updateRequest.setPerfilRisco(RiskProfile.MODERADO);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByCpfValue("12345678909")).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByEmail("outro@example.com")).thenReturn(Optional.of(outroCliente));

        // Act & Assert
        assertThatThrownBy(() -> clienteService.atualizar(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email ja cadastrado");

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve remover cliente com sucesso")
    void deveRemoverClienteComSucesso() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.empty());
        when(recomendacaoRepository.findByClienteId(1L)).thenReturn(Collections.emptyList());

        // Act
        clienteService.remover(1L);

        // Assert
        verify(clienteRepository).findById(1L);
        verify(clienteRepository).delete(cliente);
        verify(diagnosticoRepository).findByClienteId(1L);
        verify(recomendacaoRepository).findByClienteId(1L);
    }

    @Test
    @DisplayName("Deve remover cliente com diagnóstico e recomendações")
    void deveRemoverClienteComDiagnosticoERecomendacoes() {
        // Arrange
        Diagnostico diagnostico = new Diagnostico();
        ReflectionTestUtils.setField(diagnostico, "id", 1L);

        Recomendacao recomendacao = new Recomendacao();
        ReflectionTestUtils.setField(recomendacao, "id", 1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(diagnosticoRepository.findByClienteId(1L)).thenReturn(Optional.of(diagnostico));
        when(recomendacaoRepository.findByClienteId(1L)).thenReturn(List.of(recomendacao));

        // Act
        clienteService.remover(1L);

        // Assert
        verify(diagnosticoRepository).delete(diagnostico);
        verify(recomendacaoRepository).deleteAll(anyList());
        verify(clienteRepository).delete(cliente);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover cliente inexistente")
    void deveLancarExcecaoAoRemoverClienteInexistente() {
        // Arrange
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> clienteService.remover(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente nao encontrado");

        verify(clienteRepository, never()).delete(any());
    }
}
