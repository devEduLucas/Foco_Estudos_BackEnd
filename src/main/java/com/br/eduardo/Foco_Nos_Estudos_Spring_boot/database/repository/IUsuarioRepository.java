package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.UsuarioEntity;

public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
