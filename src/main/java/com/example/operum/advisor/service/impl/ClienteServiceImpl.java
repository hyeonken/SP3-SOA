package com.example.operum.advisor.service.impl;

import com.example.operum.advisor.domain.entity.Cliente;
import com.example.operum.advisor.dto.request.ClienteRequest;
import com.example.operum.advisor.dto.response.ClienteResponse;
import com.example.operum.advisor.exception.BusinessException;
import com.example.operum.advisor.exception.ResourceNotFoundException;
import com.example.operum.advisor.mapper.ClienteMapper;
import com.example.operum.advisor.repository.ClienteRepository;
import com.example.operum.advisor.repository.DiagnosticoRepository;
import com.example.operum.advisor.repository.RecomendacaoRepository;
import com.example.operum.advisor.service.ClienteService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final RecomendacaoRepository recomendacaoRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository,
                              DiagnosticoRepository diagnosticoRepository,
                              RecomendacaoRepository recomendacaoRepository) {
        this.clienteRepository = clienteRepository;
        this.diagnosticoRepository = diagnosticoRepository;
        this.recomendacaoRepository = recomendacaoRepository;
    }

    @Override
    @Transactional
    public ClienteResponse criar(ClienteRequest request) {
        validarConsentimento(request);
        validarDuplicidadeCriacao(request);
        Cliente cliente = ClienteMapper.toEntity(request);
        Cliente salvo = clienteRepository.save(cliente);
        return ClienteMapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
            .map(ClienteMapper::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorIdResponse(Long id) {
        return ClienteMapper.toResponse(buscarPorId(id));
    }

    @Override
    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        validarConsentimento(request);
        Cliente existente = buscarPorId(id);
        validarDuplicidadeAtualizacao(request, existente);
        ClienteMapper.merge(existente, request);
        Cliente atualizado = clienteRepository.save(existente);
        return ClienteMapper.toResponse(atualizado);
    }

    @Override
    @Transactional
    public void remover(Long id) {
        Cliente existente = buscarPorId(id);
        diagnosticoRepository.findByClienteId(id).ifPresent(diagnosticoRepository::delete);
        var recomendacoes = recomendacaoRepository.findByClienteId(id);
        if (!recomendacoes.isEmpty()) {
            recomendacaoRepository.deleteAll(recomendacoes);
        }
        clienteRepository.delete(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente nao encontrado"));
    }

    private void validarConsentimento(ClienteRequest request) {
        if (!Boolean.TRUE.equals(request.getConsentimentoLgpd())) {
            throw new BusinessException("Consentimento LGPD obrigatorio");
        }
    }

    private void validarDuplicidadeCriacao(ClienteRequest request) {
        if (clienteRepository.existsByCpfValue(request.getCpf())) {
            throw new BusinessException("CPF ja cadastrado");
        }
        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email ja cadastrado");
        }
    }

    private void validarDuplicidadeAtualizacao(ClienteRequest request, Cliente atual) {
        clienteRepository.findByCpfValue(request.getCpf())
            .filter(encontrado -> !encontrado.getId().equals(atual.getId()))
            .ifPresent(encontrado -> {
                throw new BusinessException("CPF ja cadastrado");
            });

        clienteRepository.findByEmail(request.getEmail())
            .filter(encontrado -> !encontrado.getId().equals(atual.getId()))
            .ifPresent(encontrado -> {
                throw new BusinessException("Email ja cadastrado");
            });
    }
}
