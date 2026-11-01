package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TextoUtilTest {

    @Test
    void normalizarRemovePontas() {
        assertEquals("João Silva", TextoUtil.normalizar("  João  Silva  "));
    }

    @Test
    void normalizarEspacosInternos() {
        assertEquals("A B", TextoUtil.normalizar("A  B"));
    }

    @Test
    void normalizarNull() {
        assertNull(TextoUtil.normalizar(null));
    }

    @Test
    void aparar() {
        assertEquals("ok", TextoUtil.aparar("  ok  "));
    }

    @Test
    void ararBrancoRetornaNull() {
        assertNull(TextoUtil.aparar("   "));
    }

    @Test
    void chaveComparacaoIgnoraAcentuacao() {
        assertEquals(TextoUtil.chaveComparacao("Matemática"), TextoUtil.chaveComparacao("matematica"));
    }

    @Test
    void chaveComparacaoIgnoraCasoEEspacos() {
        assertEquals(TextoUtil.chaveComparacao("Física Quântica"), TextoUtil.chaveComparacao("fisica quantica"));
    }
}
