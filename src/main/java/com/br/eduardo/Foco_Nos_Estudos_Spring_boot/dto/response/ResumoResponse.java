package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResumoResponse {
    private final long total;
    private final long pendentes;
    private final long emAndamento;
    private final long concluidas;
    private final long atrasadas;
}
