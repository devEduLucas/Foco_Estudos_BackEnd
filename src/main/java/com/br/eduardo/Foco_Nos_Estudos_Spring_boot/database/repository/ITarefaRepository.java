package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.TarefaEntity;

public interface ITarefaRepository extends JpaRepository<TarefaEntity, Long> {
    List<TarefaEntity> findByMateriaUsuarioIdOrderByDataPrazoAsc(Long usuarioId);

    Optional<TarefaEntity> findByIdAndMateriaUsuarioId(Long id, Long usuarioId);

    boolean existsByMateriaId(Long materiaId);
}
