package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.ANOS_MAXIMOS_PRAZO;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.ANO_MAXIMO;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.TarefaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RegraNegocioException;

/** Regras de prazo e de status da tarefa. Falham com {@link RegraNegocioException} ligada ao campo. */
@Component
public class ValidadorTarefa {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Clock clock;

    public ValidadorTarefa(Clock clock) {
        this.clock = clock;
    }

    /**
     * Ano com no máximo 4 dígitos e prazo no máximo {@code ANOS_MAXIMOS_PRAZO} anos à frente.
     * Com {@code exigirFuturo}, o prazo também não pode estar no passado.
     */
    public void validarPrazo(LocalDateTime prazo, boolean exigirFuturo) {
        if (prazo.getYear() > ANO_MAXIMO) {
            throw new RegraNegocioException("dataPrazo", "O ano do prazo deve ter no máximo 4 dígitos.");
        }
        LocalDateTime agora = LocalDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime limite = agora.plusYears(ANOS_MAXIMOS_PRAZO);
        if (prazo.isAfter(limite)) {
            throw new RegraNegocioException("dataPrazo", "O prazo pode ser no máximo " + ANOS_MAXIMOS_PRAZO
                    + " anos a partir de hoje (até " + DATA.format(limite) + ").");
        }
        if (exigirFuturo && prazo.isBefore(agora)) {
            throw new RegraNegocioException("dataPrazo", "O prazo não pode estar no passado.");
        }
    }

    /** Tarefa nova começa aberta: não pode nascer concluída. */
    public void validarStatusInicial(StatusTarefa status) {
        if (status == StatusTarefa.CONCLUIDA) {
            throw new RegraNegocioException("status", "Uma tarefa nova não pode ser criada como concluída.");
        }
    }

    /** Tarefa concluída fica congelada: para editá-la, reabra no mesmo pedido (status diferente de CONCLUIDA). */
    public void validarEdicao(TarefaEntity atual, StatusTarefa novoStatus) {
        if (atual.getStatus() == StatusTarefa.CONCLUIDA && novoStatus == StatusTarefa.CONCLUIDA) {
            throw new RegraNegocioException("status",
                    "Tarefa concluída não pode ser editada. Reabra a tarefa (Pendente ou Em andamento) para alterá-la.");
        }
    }

    /** Filtro de período: anos de 4 dígitos e data inicial não posterior à final. */
    public void validarPeriodo(LocalDateTime de, LocalDateTime ate) {
        validarAno("de", de);
        validarAno("ate", ate);
        if (de != null && ate != null && de.isAfter(ate)) {
            throw new RegraNegocioException("de", "A data inicial não pode ser posterior à data final.");
        }
    }

    private void validarAno(String campo, LocalDateTime data) {
        if (data != null && (data.getYear() < 1000 || data.getYear() > ANO_MAXIMO)) {
            throw new RegraNegocioException(campo, "O ano deve ter 4 dígitos.");
        }
    }
}
