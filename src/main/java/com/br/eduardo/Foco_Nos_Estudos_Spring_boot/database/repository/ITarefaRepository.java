package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.TarefaEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;

public interface ITarefaRepository extends JpaRepository<TarefaEntity, Long> {

    List<TarefaEntity> findByMateriaUsuarioIdOrderByDataPrazoAsc(Long usuarioId);

    Optional<TarefaEntity> findByIdAndMateriaUsuarioId(Long id, Long usuarioId);

    boolean existsByMateriaId(Long materiaId);

    long countByMateriaId(Long materiaId);

    long countByMateriaIdAndStatus(Long materiaId, StatusTarefa status);

    boolean existsByMateriaIdAndTituloIgnoreCaseAndDataPrazo(Long materiaId, String titulo, LocalDateTime dataPrazo);

    boolean existsByMateriaIdAndTituloIgnoreCaseAndDataPrazoAndIdNot(Long materiaId, String titulo,
            LocalDateTime dataPrazo, Long id);
}
