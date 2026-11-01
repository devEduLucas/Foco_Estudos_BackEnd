package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErroResponse {

    private final int status;
    private final String mensagem;
    private final List<CampoErro> campos;
    private final Instant timestamp;

    public static ErroResponse of(int status, String mensagem) {
        return of(status, mensagem, List.of());
    }

    public static ErroResponse of(int status, String mensagem, List<CampoErro> campos) {
        return new ErroResponse(status, mensagem, campos, Instant.now());
    }
}
