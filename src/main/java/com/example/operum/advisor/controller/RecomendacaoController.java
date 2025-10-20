package com.example.operum.advisor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.operum.advisor.dto.request.RecomendacaoRequest;
import com.example.operum.advisor.dto.response.RecomendacaoResponse;
import com.example.operum.advisor.service.RecomendacaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para gerenciamento de recomendações de investimento. Responsável
 * pela geração e consulta de recomendações personalizadas.
 */
@Tag(name = "Recomendações", description = "Geração e consulta de recomendações personalizadas de investimento")
@RestController
@RequestMapping("/recomendacoes")
@SecurityRequirement(name = "bearerAuth")
public class RecomendacaoController {

    private final RecomendacaoService recomendacaoService;

    public RecomendacaoController(RecomendacaoService recomendacaoService) {
        this.recomendacaoService = recomendacaoService;
    }

    @Operation(
            summary = "Gerar recomendação de investimento",
            description = "Cria uma recomendação personalizada de investimento baseada no diagnóstico "
            + "financeiro e perfil de risco do cliente. Seleciona a carteira mais adequada."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Recomendação criada com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = RecomendacaoResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos na requisição",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente ou diagnóstico não encontrado",
                content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping
    public ResponseEntity<RecomendacaoResponse> gerar(@RequestBody @Valid RecomendacaoRequest request) {
        RecomendacaoResponse response = recomendacaoService.gerar(request);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Listar recomendações por cliente",
            description = "Retorna todas as recomendações de investimento geradas para um cliente específico"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Lista de recomendações retornada com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = RecomendacaoResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/{clienteId}")
    public ResponseEntity<List<RecomendacaoResponse>> listarPorCliente(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long clienteId
    ) {
        List<RecomendacaoResponse> respostas = recomendacaoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(respostas);
    }
}
