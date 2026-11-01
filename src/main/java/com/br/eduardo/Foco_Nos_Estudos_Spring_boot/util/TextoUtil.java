package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class TextoUtil {

    private static final Pattern ESPACOS = Pattern.compile("\\s+");
    private static final Pattern MARCAS = Pattern.compile("\\p{M}+");

    private TextoUtil() {
    }

    /** Campos de uma linha (nome, título): remove as pontas e reduz espaços repetidos a um só. */
    public static String normalizar(String texto) {
        return texto == null ? null : ESPACOS.matcher(texto.strip()).replaceAll(" ");
    }

    /** Campos livres (descrição): remove as pontas e trata texto em branco como ausente. */
    public static String aparar(String texto) {
        if (texto == null) {
            return null;
        }
        String r = texto.strip();
        return r.isEmpty() ? null : r;
    }

    /** Chave para comparar nomes ignorando maiúsculas, acentos e espaços repetidos. */
    public static String chaveComparacao(String texto) {
        if (texto == null) {
            return "";
        }
        String decomposto = Normalizer.normalize(normalizar(texto), Normalizer.Form.NFD);
        return MARCAS.matcher(decomposto).replaceAll("").toLowerCase(Locale.ROOT);
    }
}
