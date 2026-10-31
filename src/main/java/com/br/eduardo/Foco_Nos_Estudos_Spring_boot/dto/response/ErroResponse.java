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
    private final List<String> detalhes;
    private final Instant timestamp;

    public static ErroResponse of(int status, String mensagem, List<String> detalhes) {
        return new ErroResponse(status, mensagem, detalhes, Instant.now());
    }
}
