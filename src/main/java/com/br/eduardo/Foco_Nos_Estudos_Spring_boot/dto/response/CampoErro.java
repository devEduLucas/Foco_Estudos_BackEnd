package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Um problema ligado a um campo: o nome é o mesmo da propriedade no JSON enviado ou do parâmetro da URL. */
@Getter
@AllArgsConstructor
public class CampoErro {

    private final String campo;
    private final String mensagem;
}
