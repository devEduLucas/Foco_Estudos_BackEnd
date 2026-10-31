package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResumoSemanalResponse {
    private final LocalDate inicioSemana;
    private final LocalDate fimSemana;
    private final long total;
    private final long concluidas;
    private final long naoConcluidas;
    private final List<TarefaResponse> tarefas;
}
