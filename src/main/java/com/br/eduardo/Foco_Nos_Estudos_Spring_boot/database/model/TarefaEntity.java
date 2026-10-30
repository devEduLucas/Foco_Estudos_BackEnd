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
@Table(name = "tarefas")
public class TarefaEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false, name = "materia_id")
    private Long materiaId;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false, name = "data_prazo")
    private LocalDate dataPrazo;

    @Column(nullable = false)
    private Number status;

    @Column(nullable = false)
    private String prioridade;
}
