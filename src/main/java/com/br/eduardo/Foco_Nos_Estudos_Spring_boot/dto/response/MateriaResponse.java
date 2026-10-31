package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MateriaResponse {
    private final Long id;
    private final String nome;
    private final String descricao;
}
