package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.MateriaRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.MateriaResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service.MateriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/materias")
public class MateriaController {

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MateriaResponse criar(@AuthenticationPrincipal Long usuarioId, @Valid @RequestBody MateriaRequest req) {
        return materiaService.criar(usuarioId, req);
    }

    @GetMapping
    public List<MateriaResponse> listar(@AuthenticationPrincipal Long usuarioId) {
        return materiaService.listar(usuarioId);
    }

    @GetMapping("/{id}")
    public MateriaResponse buscar(@AuthenticationPrincipal Long usuarioId, @PathVariable Long id) {
        return materiaService.buscar(usuarioId, id);
    }

    @PutMapping("/{id}")
    public MateriaResponse atualizar(@AuthenticationPrincipal Long usuarioId, @PathVariable Long id,
            @Valid @RequestBody MateriaRequest req) {
        return materiaService.atualizar(usuarioId, id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@AuthenticationPrincipal Long usuarioId, @PathVariable Long id) {
        materiaService.remover(usuarioId, id);
    }
}
