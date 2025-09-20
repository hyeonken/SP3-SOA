package com.example.operum.advisor.controller;

import com.example.operum.advisor.dto.request.DiagnosticoRequest;
import com.example.operum.advisor.dto.response.DiagnosticoResponse;
import com.example.operum.advisor.service.DiagnosticoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diagnostico")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    public DiagnosticoController(DiagnosticoService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }

    @PostMapping
    public ResponseEntity<DiagnosticoResponse> gerar(@RequestBody @Valid DiagnosticoRequest request) {
        DiagnosticoResponse response = diagnosticoService.gerarDiagnostico(request);
        return ResponseEntity.ok(response);
    }
}
