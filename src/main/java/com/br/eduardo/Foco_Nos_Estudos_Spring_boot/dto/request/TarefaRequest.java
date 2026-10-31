package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request;

import java.time.LocalDate;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.PrioridadeTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TarefaRequest {

    @NotBlank
    @Size(max = 150)
    private String titulo;

    @Size(max = 1000)
    private String descricao;

    @NotNull
    private LocalDate dataPrazo;

    @NotNull
    private StatusTarefa status;

    @NotNull
    private PrioridadeTarefa prioridade;

    @NotNull(message = "A tarefa deve pertencer a uma matéria!")
    private Long materiaId;
}