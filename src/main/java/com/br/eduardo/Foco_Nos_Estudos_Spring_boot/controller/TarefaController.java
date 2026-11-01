package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.StatusTarefaRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.TarefaRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.TarefaResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.PrioridadeTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service.TarefaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criar(@AuthenticationPrincipal Long usuarioId, @Valid @RequestBody TarefaRequest req) {
        return tarefaService.criar(usuarioId, req);
    }

    @GetMapping
    public List<TarefaResponse> listar(@AuthenticationPrincipal Long usuarioId,
            @RequestParam(required = false) Long materiaId,
            @RequestParam(required = false) StatusTarefa status,
            @RequestParam(required = false) PrioridadeTarefa prioridade,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime de,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ate,
            @RequestParam(required = false) Boolean atrasadas) {
        return tarefaService.listar(usuarioId, materiaId, status, prioridade, de, ate, atrasadas);
    }

    @GetMapping("/hoje")
    public List<TarefaResponse> hoje(@AuthenticationPrincipal Long usuarioId) {
        return tarefaService.listarHoje(usuarioId);
    }

    @GetMapping("/proximos-prazos")
    public List<TarefaResponse> proximosPrazos(@AuthenticationPrincipal Long usuarioId,
            @RequestParam(defaultValue = "7") int dias) {
        return tarefaService.listarProximosPrazos(usuarioId, dias);
    }

    @GetMapping("/{id}")
    public TarefaResponse buscar(@AuthenticationPrincipal Long usuarioId, @PathVariable Long id) {
        return tarefaService.buscar(usuarioId, id);
    }

    @PutMapping("/{id}")
    public TarefaResponse atualizar(@AuthenticationPrincipal Long usuarioId, @PathVariable Long id,
            @Valid @RequestBody TarefaRequest req) {
        return tarefaService.atualizar(usuarioId, id, req);
    }

    @PatchMapping("/{id}/status")
    public TarefaResponse atualizarStatus(@AuthenticationPrincipal Long usuarioId, @PathVariable Long id,
            @Valid @RequestBody StatusTarefaRequest req) {
        return tarefaService.atualizarStatus(usuarioId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@AuthenticationPrincipal Long usuarioId, @PathVariable Long id) {
        tarefaService.remover(usuarioId, id);
    }
}
