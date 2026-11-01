package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusTarefaRequest {

    @NotNull(message = "Informe o status da tarefa.")
    private StatusTarefa status;
}
