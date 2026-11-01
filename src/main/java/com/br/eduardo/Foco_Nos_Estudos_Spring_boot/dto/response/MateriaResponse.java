package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateriaResponse {

    private final Long id;
    private final String nome;
    private final String descricao;
    private final long totalTarefas;
    /** Tarefas com status PENDENTE. */
    private final long tarefasPendentes;
}
