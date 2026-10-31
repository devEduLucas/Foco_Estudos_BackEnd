package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private final String token;
    private final String tipo;
    private final Long usuarioId;
    private final String nome;
}
