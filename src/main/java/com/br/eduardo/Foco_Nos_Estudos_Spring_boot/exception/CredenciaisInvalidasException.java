package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
