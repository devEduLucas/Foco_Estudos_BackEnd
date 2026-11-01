package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception;

/** Violação de regra de negócio (HTTP 400). Se informar o campo, o frontend mostra a mensagem junto dele. */
public class RegraNegocioException extends RuntimeException {

    private final String campo;

    public RegraNegocioException(String mensagem) {
        this(null, mensagem);
    }

    public RegraNegocioException(String campo, String mensagem) {
        super(mensagem);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
