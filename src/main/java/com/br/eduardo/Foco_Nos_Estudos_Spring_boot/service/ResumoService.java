package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
        LocalDate hoje = LocalDate.now(clock);
        List<TarefaEntity> tarefas = tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId);
        return new ResumoResponse(
                tarefas.size(),
                contar(tarefas, StatusTarefa.PENDENTE),
                contar(tarefas, StatusTarefa.EM_ANDAMENTO),
                contar(tarefas, StatusTarefa.CONCLUIDA),
                tarefas.stream().filter(t -> atrasada(t, hoje)).count());
    }

    /** Semana de segunda a domingo que contém hoje, pelas tarefas com prazo nela. */
    @Transactional(readOnly = true)
    public ResumoSemanalResponse semanal(Long usuarioId) {
        LocalDate hoje = LocalDate.now(clock);
        LocalDate inicio = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fim = inicio.plusDays(6);
        List<TarefaEntity> daSemana = tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId).stream()
                .filter(t -> !t.getDataPrazo().isBefore(inicio) && !t.getDataPrazo().isAfter(fim))
                .toList();
        long concluidas = contar(daSemana, StatusTarefa.CONCLUIDA);
        List<TarefaResponse> tarefas = daSemana.stream()
                .map(t -> new TarefaResponse(t.getId(), t.getMateria().getId(), t.getTitulo(), t.getDescricao(),
                        t.getDataPrazo(), t.getStatus(), t.getPrioridade(), atrasada(t, hoje)))
                .toList();
        return new ResumoSemanalResponse(inicio, fim, daSemana.size(), concluidas, daSemana.size() - concluidas,
                tarefas);
    }

    private long contar(List<TarefaEntity> tarefas, StatusTarefa status) {
        return tarefas.stream().filter(t -> t.getStatus() == status).count();
    }

    private boolean atrasada(TarefaEntity t, LocalDate hoje) {
        return t.getStatus() != StatusTarefa.CONCLUIDA && t.getDataPrazo().isBefore(hoje);
    }
}
