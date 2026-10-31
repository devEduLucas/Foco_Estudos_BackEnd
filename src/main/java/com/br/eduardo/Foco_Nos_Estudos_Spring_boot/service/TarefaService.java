package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.MateriaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.TarefaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.IMateriaRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.ITarefaRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.StatusTarefaRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.TarefaRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.TarefaResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RecursoNaoEncontradoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RegraNegocioException;

@Service
public class TarefaService {

    private final ITarefaRepository tarefaRepository;
    private final IMateriaRepository materiaRepository;
    private final Clock clock;

    public TarefaService(ITarefaRepository tarefaRepository, IMateriaRepository materiaRepository, Clock clock) {
        this.tarefaRepository = tarefaRepository;
        this.materiaRepository = materiaRepository;
        this.clock = clock;
    }

    @Transactional
    public TarefaResponse criar(Long usuarioId, TarefaRequest req) {
        if (req.getDataPrazo().isBefore(LocalDate.now(clock))) {
            throw new RegraNegocioException("O prazo não pode estar no passado");
        }
        TarefaEntity t = new TarefaEntity();
        preencher(t, usuarioId, req);
        return paraResponse(tarefaRepository.save(t));
    }

    /** Filtros opcionais. shortcut: filtra em memória, ok para volume de estudo pessoal; migrar para query se crescer. */
    @Transactional(readOnly = true)
    public List<TarefaResponse> listar(Long usuarioId, Long materiaId, StatusTarefa status) {
        return tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId).stream()
                .filter(t -> materiaId == null || t.getMateria().getId().equals(materiaId))
                .filter(t -> status == null || t.getStatus() == status)
                .map(this::paraResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TarefaResponse buscar(Long usuarioId, Long id) {
        return paraResponse(obter(usuarioId, id));
    }

    @Transactional
    public TarefaResponse atualizar(Long usuarioId, Long id, TarefaRequest req) {
        TarefaEntity t = obter(usuarioId, id);
        preencher(t, usuarioId, req);
        return paraResponse(tarefaRepository.save(t));
    }

    @Transactional
    public TarefaResponse atualizarStatus(Long usuarioId, Long id, StatusTarefaRequest req) {
        TarefaEntity t = obter(usuarioId, id);
        t.setStatus(req.getStatus());
        return paraResponse(tarefaRepository.save(t));
    }

    @Transactional
    public void remover(Long usuarioId, Long id) {
        tarefaRepository.delete(obter(usuarioId, id));
    }

    private TarefaEntity obter(Long usuarioId, Long id) {
        return tarefaRepository.findByIdAndMateriaUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada"));
    }

    private void preencher(TarefaEntity t, Long usuarioId, TarefaRequest req) {
        MateriaEntity materia = materiaRepository.findByIdAndUsuarioId(req.getMateriaId(), usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria não encontrada"));
        t.setMateria(materia);
        t.setTitulo(req.getTitulo().trim());
        t.setDescricao(req.getDescricao());
        t.setDataPrazo(req.getDataPrazo());
        t.setStatus(req.getStatus());
        t.setPrioridade(req.getPrioridade());
    }

    private TarefaResponse paraResponse(TarefaEntity t) {
        boolean atrasada = t.getStatus() != StatusTarefa.CONCLUIDA && t.getDataPrazo().isBefore(LocalDate.now(clock));
        return new TarefaResponse(t.getId(), t.getMateria().getId(), t.getTitulo(), t.getDescricao(),
                t.getDataPrazo(), t.getStatus(), t.getPrioridade(), atrasada);
    }
}
