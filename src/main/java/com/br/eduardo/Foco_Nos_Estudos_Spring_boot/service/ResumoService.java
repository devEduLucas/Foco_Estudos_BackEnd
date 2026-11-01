package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.TarefaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.ITarefaRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.ResumoResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.ResumoSemanalResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.TarefaResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;

@Service
public class ResumoService {

    private final ITarefaRepository tarefaRepository;
    private final Clock clock;

    public ResumoService(ITarefaRepository tarefaRepository, Clock clock) {
        this.tarefaRepository = tarefaRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public ResumoResponse resumo(Long usuarioId) {
        LocalDateTime agora = LocalDateTime.now(clock);
        List<TarefaEntity> tarefas = tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId);
        return new ResumoResponse(
                tarefas.size(),
                contar(tarefas, StatusTarefa.PENDENTE),
                contar(tarefas, StatusTarefa.EM_ANDAMENTO),
                contar(tarefas, StatusTarefa.CONCLUIDA),
                tarefas.stream().filter(t -> t.estaAtrasada(agora)).count());
    }

    /** Semana de segunda a domingo que contém hoje, pelas tarefas com prazo nela. */
    @Transactional(readOnly = true)
    public ResumoSemanalResponse semanal(Long usuarioId) {
        LocalDateTime agora = LocalDateTime.now(clock);
        LocalDate inicio = LocalDate.now(clock).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fim = inicio.plusDays(6);
        LocalDateTime de = inicio.atStartOfDay();
        LocalDateTime ate = fim.plusDays(1).atStartOfDay();
        List<TarefaEntity> daSemana = tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId).stream()
                .filter(t -> !t.getDataPrazo().isBefore(de) && t.getDataPrazo().isBefore(ate))
                .toList();
        long planejadas = daSemana.size();
        long concluidas = contar(daSemana, StatusTarefa.CONCLUIDA);
        List<TarefaResponse> tarefas = daSemana.stream().map(t -> TarefaResponse.de(t, agora)).toList();
        return new ResumoSemanalResponse(inicio, fim, planejadas, planejadas, concluidas, planejadas - concluidas,
                tarefas);
    }

    private long contar(List<TarefaEntity> tarefas, StatusTarefa status) {
        return tarefas.stream().filter(t -> t.getStatus() == status).count();
    }
}
