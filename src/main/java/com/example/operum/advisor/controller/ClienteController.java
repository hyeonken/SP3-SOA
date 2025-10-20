package com.example.operum.advisor.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.operum.advisor.dto.request.ClienteRequest;
import com.example.operum.advisor.dto.response.ClienteResponse;
import com.example.operum.advisor.service.ClienteService;

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
 * Controller para gerenciamento de clientes. Fornece endpoints para CRUD
 * completo de clientes com conformidade LGPD.
 */
@Tag(name = "Clientes", description = "Gerenciamento de clientes e informações pessoais com conformidade LGPD")
@RestController
@RequestMapping("/clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
            summary = "Criar novo cliente",
            description = "Cadastra um novo cliente no sistema. Requer consentimento LGPD e validação de CPF único."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Cliente criado com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ClienteResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos na requisição",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "422",
                description = "CPF ou email já cadastrado",
                content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@RequestBody @Valid ClienteRequest request) {
        ClienteResponse response = clienteService.criar(request);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna lista completa de clientes cadastrados no sistema"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Lista de clientes retornada com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ClienteResponse.class)
                )
        )
    })
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Retorna os detalhes de um cliente específico pelo seu identificador"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cliente encontrado",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ClienteResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json")
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(clienteService.buscarPorIdResponse(id));
    }

    @Operation(
            summary = "Atualizar cliente",
            description = "Atualiza os dados de um cliente existente. Mantém validações de CPF e email únicos."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Cliente atualizado com sucesso",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ClienteResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
                responseCode = "422",
                description = "CPF ou email já em uso por outro cliente",
                content = @Content(mediaType = "application/json")
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id,
            @RequestBody @Valid ClienteRequest request
    ) {
        ClienteResponse response = clienteService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Remover cliente",
            description = "Remove um cliente do sistema (soft delete). Mantém conformidade com LGPD."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "204",
                description = "Cliente removido com sucesso"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json")
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do cliente", required = true, example = "1")
            @PathVariable Long id
    ) {
        clienteService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
