package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.MateriaEntity;

public interface IMateriaRepository extends JpaRepository<MateriaEntity, Long> {

    List<MateriaEntity> findByUsuarioIdOrderByNomeAsc(Long usuarioId);

    Optional<MateriaEntity> findByIdAndUsuarioId(Long id, Long usuarioId);

    long countByUsuarioId(Long usuarioId);
}
