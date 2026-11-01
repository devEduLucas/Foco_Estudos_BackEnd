package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception;

/** Conflito com dados existentes, como duplicidade (HTTP 409). O campo é opcional. */
public class ConflitoException extends RuntimeException {

    private final String campo;

    public ConflitoException(String mensagem) {
        this(null, mensagem);
    }

    public ConflitoException(String campo, String mensagem) {
        super(mensagem);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
