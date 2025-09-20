package com.example.operum.advisor.controller;

import com.example.operum.advisor.dto.request.RecomendacaoRequest;
import com.example.operum.advisor.dto.response.RecomendacaoResponse;
import com.example.operum.advisor.service.RecomendacaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recomendacoes")
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    public RecomendacaoController(RecomendacaoService recomendacaoService) {
        this.recomendacaoService = recomendacaoService;
    }

    @PostMapping
    public ResponseEntity<RecomendacaoResponse> gerar(@RequestBody @Valid RecomendacaoRequest request) {
        RecomendacaoResponse response = recomendacaoService.gerar(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<List<RecomendacaoResponse>> listarPorCliente(@PathVariable Long clienteId) {
        List<RecomendacaoResponse> respostas = recomendacaoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(respostas);
    }
}
