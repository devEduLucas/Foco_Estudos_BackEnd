package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Table(name = "materias")
public class MateriaEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(nullable = false, name = "usuario_id")
    private Long usuarioId;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;
}
