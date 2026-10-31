package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MateriaRequest {

    @NotBlank
    @Size(max = 150)
    private String nome;

    @Size(max = 1000)
    private String descricao;
}
