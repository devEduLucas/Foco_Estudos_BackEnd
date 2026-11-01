package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import java.time.LocalDateTime;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.TarefaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.PrioridadeTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TarefaResponse {

    private final Long id;
    /** Mantido por compatibilidade; o frontend usa {@code materia}. */
    private final Long materiaId;
    private final MateriaResumo materia;
    private final String titulo;
    private final String descricao;
    private final LocalDateTime dataPrazo;
    private final StatusTarefa status;
    private final PrioridadeTarefa prioridade;
    private final boolean atrasada;

    @Getter
    @AllArgsConstructor
    public static class MateriaResumo {
        private final Long id;
        private final String nome;
    }

    /** Deve ser chamado dentro de uma transação: lê a matéria, que é carregada sob demanda. */
    public static TarefaResponse de(TarefaEntity t, LocalDateTime agora) {
        return new TarefaResponse(t.getId(), t.getMateria().getId(),
                new MateriaResumo(t.getMateria().getId(), t.getMateria().getNome()),
                t.getTitulo(), t.getDescricao(), t.getDataPrazo(), t.getStatus(), t.getPrioridade(),
                t.estaAtrasada(agora));
    }
}
