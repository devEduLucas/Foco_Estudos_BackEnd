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
@Table(name = "usuarios")
public class UsuarioEntity {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;

    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, name = "senha_hash")
    private String senhaHash;

    private LocalDate criadoEm;
}
