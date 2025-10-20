package com.example.operum.advisor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.operum.advisor.dto.request.DiagnosticoRequest;
import com.example.operum.advisor.dto.response.DiagnosticoResponse;
import com.example.operum.advisor.service.DiagnosticoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controller para gerenciamento de diagnósticos financeiros. Responsável pela
 * avaliação da situação financeira do cliente.
 */
@Tag(name = "Diagnósticos", description = "Geração de diagnósticos financeiros e avaliação de perfil de risco")
@RestController
@RequestMapping("/diagnostico")
@SecurityRequirement(name = "bearerAuth")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    public DiagnosticoController(DiagnosticoService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }

    @Operation(
            summary = "Gerar diagnóstico financeiro",
            description = "Cria ou atualiza o diagnóstico financeiro de um cliente baseado em suas informações "
            + "financeiras atuais. Calcula score de crédito, capacidade de investimento e perfil de risco."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Diagnóstico gerado com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = DiagnosticoResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos na requisição",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping
    public ResponseEntity<DiagnosticoResponse> gerar(@RequestBody @Valid DiagnosticoRequest request) {
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);
        return ResponseEntity.ok(response);
    }
}
