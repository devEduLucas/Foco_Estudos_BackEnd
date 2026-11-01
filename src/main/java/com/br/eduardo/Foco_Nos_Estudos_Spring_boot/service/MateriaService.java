package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.MAX_MATERIAS_POR_USUARIO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.MateriaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.TarefaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.IMateriaRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.ITarefaRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.IUsuarioRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.MateriaRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.MateriaResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.ConflitoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RecursoNaoEncontradoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RegraNegocioException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.TextoUtil;

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
        if (materiaRepository.countByUsuarioId(usuarioId) >= MAX_MATERIAS_POR_USUARIO) {
            throw new RegraNegocioException("Você atingiu o limite de " + MAX_MATERIAS_POR_USUARIO
                    + " matérias. Exclua uma que não usa mais.");
        }
        validarNomeUnico(usuarioId, req.getNome(), null);
        MateriaEntity m = new MateriaEntity();
        m.setUsuario(usuarioRepository.getReferenceById(usuarioId));
        preencher(m, req);
        return paraResponse(materiaRepository.save(m));
    }

    @Transactional(readOnly = true)
    public List<MateriaResponse> listar(Long usuarioId) {
        List<TarefaEntity> tarefas = tarefaRepository.findByMateriaUsuarioIdOrderByDataPrazoAsc(usuarioId);
        Map<Long, Long> totais = tarefas.stream()
                .collect(Collectors.groupingBy(t -> t.getMateria().getId(), Collectors.counting()));
        Map<Long, Long> pendentes = tarefas.stream()
                .filter(t -> t.getStatus() == StatusTarefa.PENDENTE)
                .collect(Collectors.groupingBy(t -> t.getMateria().getId(), Collectors.counting()));
        return materiaRepository.findByUsuarioIdOrderByNomeAsc(usuarioId).stream()
                .map(m -> new MateriaResponse(m.getId(), m.getNome(), m.getDescricao(),
                        totais.getOrDefault(m.getId(), 0L), pendentes.getOrDefault(m.getId(), 0L)))
                .toList();
    }

    @Transactional(readOnly = true)
    public MateriaResponse buscar(Long usuarioId, Long id) {
        return paraResponse(obter(usuarioId, id));
    }

    @Transactional
    public MateriaResponse atualizar(Long usuarioId, Long id, MateriaRequest req) {
        MateriaEntity m = obter(usuarioId, id);
        validarNomeUnico(usuarioId, req.getNome(), id);
        preencher(m, req);
        return paraResponse(materiaRepository.save(m));
    }

    @Transactional
    public void remover(Long usuarioId, Long id) {
        MateriaEntity m = obter(usuarioId, id);
        if (tarefaRepository.existsByMateriaId(id)) {
            throw new ConflitoException("Esta matéria tem tarefas. Exclua ou mova as tarefas antes de removê-la.");
        }
        materiaRepository.delete(m);
    }

    /** Matéria de outro usuário é tratada como inexistente (404), sem vazar que existe. */
    private MateriaEntity obter(Long usuarioId, Long id) {
        return materiaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Matéria não encontrada."));
    }

    /** Compara sem diferenciar maiúsculas, acentos e espaços repetidos, para o mesmo usuário. */
    private void validarNomeUnico(Long usuarioId, String nome, Long idAtual) {
        String chave = TextoUtil.chaveComparacao(nome);
        boolean existe = materiaRepository.findByUsuarioIdOrderByNomeAsc(usuarioId).stream()
                .filter(m -> !m.getId().equals(idAtual))
                .anyMatch(m -> TextoUtil.chaveComparacao(m.getNome()).equals(chave));
        if (existe) {
            throw new ConflitoException("nome", "Você já tem uma matéria com este nome.");
        }
    }

    private void preencher(MateriaEntity m, MateriaRequest req) {
        m.setNome(TextoUtil.normalizar(req.getNome()));
        m.setDescricao(TextoUtil.aparar(req.getDescricao()));
    }

    private MateriaResponse paraResponse(MateriaEntity m) {
        return new MateriaResponse(m.getId(), m.getNome(), m.getDescricao(),
                tarefaRepository.countByMateriaId(m.getId()),
                tarefaRepository.countByMateriaIdAndStatus(m.getId(), StatusTarefa.PENDENTE));
    }
}
