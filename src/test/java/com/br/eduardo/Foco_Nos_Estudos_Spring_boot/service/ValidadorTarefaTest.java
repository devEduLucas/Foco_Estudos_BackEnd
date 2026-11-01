package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.TarefaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.PrioridadeTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RegraNegocioException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio;

class ValidadorTarefaTest {

    // Relógio fixo: 2026-01-15T12:00:00Z
    private static final Instant AGORA = Instant.parse("2026-01-15T12:00:00Z");
    private static final Clock CLOCK = Clock.fixed(AGORA, ZoneOffset.UTC);
    private static final LocalDateTime HOJE_MEIO_DIA = LocalDateTime.ofInstant(AGORA, ZoneOffset.UTC);

    private ValidadorTarefa validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorTarefa(CLOCK);
    }

    @Test
    void prazoFuturoValido() {
        assertDoesNotThrow(() -> validador.validarPrazo(HOJE_MEIO_DIA.plusDays(1), true));
    }

    @Test
    void prazoNoPassadoComExigirFuturo() {
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> validador.validarPrazo(HOJE_MEIO_DIA.minusMinutes(1), true));
        assertEquals("dataPrazo", ex.getCampo());
        assertTrue(ex.getMessage().contains("passado"));
    }

    @Test
    void prazoNoPassadoSemExigirFuturo() {
        assertDoesNotThrow(() -> validador.validarPrazo(HOJE_MEIO_DIA.minusDays(1), false));
    }

    @Test
    void prazoAlemDe10Anos() {
        LocalDateTime onzeAnos = HOJE_MEIO_DIA.plusYears(RegrasNegocio.ANOS_MAXIMOS_PRAZO).plusDays(1);
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> validador.validarPrazo(onzeAnos, false));
        assertEquals("dataPrazo", ex.getCampo());
        assertTrue(ex.getMessage().contains("10"));
    }

    @Test
    void prazoComAnoMaisDe4Digitos() {
        LocalDateTime futuroLonge = LocalDateTime.of(10000, 1, 1, 0, 0);
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> validador.validarPrazo(futuroLonge, false));
        assertEquals("dataPrazo", ex.getCampo());
    }

    @Test
    void statusInicialConcluida() {
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> validador.validarStatusInicial(StatusTarefa.CONCLUIDA));
        assertEquals("status", ex.getCampo());
    }

    @Test
    void statusInicialPendente() {
        assertDoesNotThrow(() -> validador.validarStatusInicial(StatusTarefa.PENDENTE));
    }

    @Test
    void edicaoConcluirConcluida() {
        TarefaEntity t = tarefa(StatusTarefa.CONCLUIDA);
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> validador.validarEdicao(t, StatusTarefa.CONCLUIDA));
        assertEquals("status", ex.getCampo());
    }

    @Test
    void edicaoReabrirConcluida() {
        TarefaEntity t = tarefa(StatusTarefa.CONCLUIDA);
        assertDoesNotThrow(() -> validador.validarEdicao(t, StatusTarefa.PENDENTE));
    }

    @Test
    void periodoInicialPosteriorFinal() {
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> validador.validarPeriodo(HOJE_MEIO_DIA.plusDays(2), HOJE_MEIO_DIA.plusDays(1)));
        assertEquals("de", ex.getCampo());
    }

    @Test
    void periodoValido() {
        assertDoesNotThrow(() -> validador.validarPeriodo(HOJE_MEIO_DIA, HOJE_MEIO_DIA.plusDays(7)));
    }

    @Test
    void periodoComAnoInvalido() {
        assertThrows(RegraNegocioException.class,
                () -> validador.validarPeriodo(LocalDateTime.of(10000, 1, 1, 0, 0), null));
    }

    private TarefaEntity tarefa(StatusTarefa status) {
        TarefaEntity t = new TarefaEntity();
        t.setStatus(status);
        t.setPrioridade(PrioridadeTarefa.MEDIA);
        t.setDataPrazo(HOJE_MEIO_DIA.plusDays(1));
        return t;
    }
}
