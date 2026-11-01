package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util;

/** Limites e formatos das regras de negócio num só lugar. Usados pelos DTOs, pelos services e pelos testes. */
public final class RegrasNegocio {

    private RegrasNegocio() {
    }

    // Usuário
    public static final int NOME_USUARIO_MIN = 2;
    public static final int NOME_USUARIO_MAX = 150;
    public static final int EMAIL_MAX = 150;
    public static final int SENHA_MIN = 8;
    public static final int SENHA_MAX = 72;
    /** O BCrypt só considera os 72 primeiros bytes; acima disso o Spring recusa a senha. */
    public static final int SENHA_MAX_BYTES = 72;

    // Matéria
    public static final int NOME_MATERIA_MIN = 2;
    public static final int NOME_MATERIA_MAX = 150;
    public static final int DESCRICAO_MATERIA_MAX = 1000;
    public static final int MAX_MATERIAS_POR_USUARIO = 100;

    // Tarefa
    public static final int TITULO_TAREFA_MIN = 3;
    public static final int TITULO_TAREFA_MAX = 150;
    public static final int DESCRICAO_TAREFA_MAX = 1000;
    public static final int ANOS_MAXIMOS_PRAZO = 10;
    public static final int ANO_MAXIMO = 9999;

    // Consultas
    public static final int DIAS_PROXIMOS_PADRAO = 7;
    public static final int DIAS_PROXIMOS_MAX = 90;

    // Formatos
    public static final String PADRAO_NOME_PESSOA = "^[\\p{L}][\\p{L}\\p{M} .''-]*$";
    public static final String PADRAO_EMAIL = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$";
    public static final String PADRAO_SENHA = "^(?=.*\\p{L})(?=.*\\d).+$";
    public static final String PADRAO_TEXTO_COM_CONTEUDO = "^(?s).*[\\p{L}\\p{N}].*$";
}
