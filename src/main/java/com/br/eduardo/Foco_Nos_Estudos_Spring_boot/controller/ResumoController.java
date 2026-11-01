package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.ResumoResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.ResumoSemanalResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service.ResumoService;

@RestController
@RequestMapping("/resumo")
public class ResumoController {

    private final ResumoService resumoService;

    public ResumoController(ResumoService resumoService) {
        this.resumoService = resumoService;
    }

    @GetMapping
    public ResumoResponse resumo(@AuthenticationPrincipal Long usuarioId) {
        return resumoService.resumo(usuarioId);
    }

    @GetMapping({ "/semanal", "/semana" })
    public ResumoSemanalResponse semanal(@AuthenticationPrincipal Long usuarioId) {
        return resumoService.semanal(usuarioId);
    }
}
