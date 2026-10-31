package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception;

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
