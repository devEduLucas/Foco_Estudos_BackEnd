package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.MateriaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.IMateriaRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.ITarefaRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.IUsuarioRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.MateriaRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.MateriaResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.ConflitoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RecursoNaoEncontradoException;

@Service
public class MateriaService {

    private final IMateriaRepository materiaRepository;
    private final ITarefaRepository tarefaRepository;
    private final IUsuarioRepository usuarioRepository;

    public MateriaService(IMateriaRepository materiaRepository, ITarefaRepository tarefaRepository,
            IUsuarioRepository usuarioRepository) {
        this.materiaRepository = materiaRepository;
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public MateriaResponse criar(Long usuarioId, MateriaRequest req) {
        MateriaEntity m = new MateriaEntity();
        m.setUsuario(usuarioRepository.getReferenceById(usuarioId));
        preencher(m, req);
        return paraResponse(materiaRepository.save(m));
    }

    @Transactional(readOnly = true)
    public List<MateriaResponse> listar(Long usuarioId) {
        return materiaRepository.findByUsuarioIdOrderByNomeAsc(usuarioId).stream().map(this::paraResponse).toList();
    }

    @Transactional(readOnly = true)
    public MateriaResponse buscar(Long usuarioId, Long id) {
        return paraResponse(obter(usuarioId, id));
    }

    @Transactional
    public MateriaResponse atualizar(Long usuarioId, Long id, MateriaRequest req) {
        MateriaEntity m = obter(usuarioId, id);
        preencher(m, req);
        return paraResponse(materiaRepository.save(m));
    }

    @Transactional
    public void remover(Long usuarioId, Long id) {
        MateriaEntity m = obter(usuarioId, id);
        if (tarefaRepository.existsByMateriaId(id)) {
            throw new ConflitoException("Matéria possui tarefas; remova-as antes");
        }
        materiaRepository.delete(m);
    }

    /** Matéria de outro usuário é tratada como inexistente (404), sem vazar que existe. */
    private MateriaEntity obter(Long usuarioId, Long id) {
        return materiaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria não encontrada"));
    }

    private void preencher(MateriaEntity m, MateriaRequest req) {
        m.setNome(req.getNome().trim());
        m.setDescricao(req.getDescricao());
    }

    private MateriaResponse paraResponse(MateriaEntity m) {
        return new MateriaResponse(m.getId(), m.getNome(), m.getDescricao());
    }
}
