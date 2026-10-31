package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import java.time.LocalDate;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.PrioridadeTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TarefaResponse {
    private final Long id;
    private final Long materiaId;
    private final String titulo;
    private final String descricao;
    private final LocalDate dataPrazo;
    private final StatusTarefa status;
    private final PrioridadeTarefa prioridade;
    private final boolean atrasada;
}
