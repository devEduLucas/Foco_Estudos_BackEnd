package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.DIAS_PROXIMOS_MAX;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.PrioridadeTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.ConflitoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RecursoNaoEncontradoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RegraNegocioException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.TextoUtil;

@Service
public class TarefaService {

    private final ITarefaRepository tarefaRepository;
    private final IMateriaRepository materiaRepository;
    private final ValidadorTarefa validador;
    private final Clock clock;

    public TarefaService(ITarefaRepository tarefaRepository, IMateriaRepository materiaRepository,
            ValidadorTarefa validador, Clock clock) {
        this.tarefaRepository = tarefaRepository;
        this.materiaRepository = materiaRepository;
        this.validador = validador;
        this.clock = clock;
    }

    @Transactional
    public TarefaResponse criar(Long usuarioId, TarefaRequest req) {
        validador.validarStatusInicial(req.getStatus());
        validador.validarPrazo(req.getDataPrazo(), true);
        MateriaEntity materia = obterMateria(usuarioId, req.getMateriaId());
        validarDuplicidade(materia.getId(), req, null);
        TarefaEntity t = new TarefaEntity();
        preencher(t, materia, req);
        return responder(tarefaRepository.save(t));
    }

    /** Filtros opcionais. shortcut: filtra em memória, ok para volume de estudo pessoal; migrar para query se crescer. */
    @Transactional(readOnly = true)
    public List<TarefaResponse> listar(Long usuarioId, Long materiaId, StatusTarefa status,
            PrioridadeTarefa prioridade, LocalDateTime de, LocalDateTime ate, Boolean atrasadas) {
        validador.validarPeriodo(de, ate);
        LocalDateTime agora = agora();
        return tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId).stream()
                .filter(t -> materiaId == null || t.getMateria().getId().equals(materiaId))
                .filter(t -> status == null || t.getStatus() == status)
                .filter(t -> prioridade == null || t.getPrioridade() == prioridade)
                .filter(t -> de == null || !t.getDataPrazo().isBefore(de))
                .filter(t -> ate == null || !t.getDataPrazo().isAfter(ate))
                .filter(t -> !Boolean.TRUE.equals(atrasadas) || t.estaAtrasada(agora))
                .map(t -> TarefaResponse.de(t, agora))
                .toList();
    }

    /** Tarefas com prazo hoje (qualquer status) mais as atrasadas ainda abertas. */
    @Transactional(readOnly = true)
    public List<TarefaResponse> listarHoje(Long usuarioId) {
        LocalDateTime agora = agora();
        LocalDateTime inicio = LocalDate.now(clock).atStartOfDay();
        LocalDateTime fim = inicio.plusDays(1);
        return tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId).stream()
                .filter(t -> t.getDataPrazo().isBefore(fim))
                .filter(t -> !t.getDataPrazo().isBefore(inicio) || t.getStatus() != StatusTarefa.CONCLUIDA)
                .map(t -> TarefaResponse.de(t, agora))
                .toList();
    }

    /** Tarefas abertas com prazo de amanhã até daqui a {@code dias} dias (hoje fica em listarHoje). */
    @Transactional(readOnly = true)
    public List<TarefaResponse> listarProximosPrazos(Long usuarioId, int dias) {
        if (dias < 1 || dias > DIAS_PROXIMOS_MAX) {
            throw new RegraNegocioException("dias", "Informe de 1 a " + DIAS_PROXIMOS_MAX + " dias.");
        }
        LocalDateTime agora = agora();
        LocalDateTime inicio = LocalDate.now(clock).plusDays(1).atStartOfDay();
        LocalDateTime fim = LocalDate.now(clock).plusDays(dias + 1L).atStartOfDay();
        return tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId).stream()
                .filter(t -> !t.getDataPrazo().isBefore(inicio) && t.getDataPrazo().isBefore(fim))
                .filter(t -> t.getStatus() != StatusTarefa.CONCLUIDA)
                .map(t -> TarefaResponse.de(t, agora))
                .toList();
    }

    @Transactional(readOnly = true)
    public TarefaResponse buscar(Long usuarioId, Long id) {
        return responder(obter(usuarioId, id));
    }

    @Transactional
    public TarefaResponse atualizar(Long usuarioId, Long id, TarefaRequest req) {
        TarefaEntity t = obter(usuarioId, id);
        validador.validarEdicao(t, req.getStatus());
        boolean prazoMudou = !req.getDataPrazo().equals(t.getDataPrazo());
        validador.validarPrazo(req.getDataPrazo(), prazoMudou && req.getStatus() != StatusTarefa.CONCLUIDA);
        MateriaEntity materia = obterMateria(usuarioId, req.getMateriaId());
        validarDuplicidade(materia.getId(), req, id);
        preencher(t, materia, req);
        return responder(tarefaRepository.save(t));
    }

    /** Qualquer mudança entre os três status é permitida (o checkbox conclui e reabre); repetir o status é inofensivo. */
    @Transactional
    public TarefaResponse atualizarStatus(Long usuarioId, Long id, StatusTarefaRequest req) {
        TarefaEntity t = obter(usuarioId, id);
        t.setStatus(req.getStatus());
        return responder(tarefaRepository.save(t));
    }

    @Transactional
    public void remover(Long usuarioId, Long id) {
        tarefaRepository.delete(obter(usuarioId, id));
    }

    private TarefaEntity obter(Long usuarioId, Long id) {
        return tarefaRepository.findByIdAndMateriaUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa não encontrada."));
    }

    /** Matéria de outro usuário é tratada como inexistente, sem revelar que existe. */
    private MateriaEntity obterMateria(Long usuarioId, Long materiaId) {
        return materiaRepository.findByIdAndUsuarioId(materiaId, usuarioId)
                .orElseThrow(() -> new RegraNegocioException("materiaId",
                        "Matéria não encontrada. Selecione uma matéria existente."));
    }

    private void validarDuplicidade(Long materiaId, TarefaRequest req, Long idAtual) {
        String titulo = TextoUtil.normalizar(req.getTitulo());
        boolean existe = idAtual == null
                ? tarefaRepository.existsByMateriaIdAndTituloIgnoreCaseAndDataPrazo(materiaId, titulo,
                        req.getDataPrazo())
                : tarefaRepository.existsByMateriaIdAndTituloIgnoreCaseAndDataPrazoAndIdNot(materiaId, titulo,
                        req.getDataPrazo(), idAtual);
        if (existe) {
            throw new ConflitoException("titulo", "Já existe uma tarefa com este título e prazo nesta matéria.");
        }
    }

    private void preencher(TarefaEntity t, MateriaEntity materia, TarefaRequest req) {
        t.setMateria(materia);
        t.setTitulo(TextoUtil.normalizar(req.getTitulo()));
        t.setDescricao(TextoUtil.aparar(req.getDescricao()));
        t.setDataPrazo(req.getDataPrazo());
        t.setStatus(req.getStatus());
        t.setPrioridade(req.getPrioridade());
    }

    private TarefaResponse responder(TarefaEntity t) {
        return TarefaResponse.de(t, agora());
    }

    private LocalDateTime agora() {
        return LocalDateTime.now(clock);
    }
}
