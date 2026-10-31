package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception;

public class ConflitoException extends RuntimeException {
    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
