package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request;

import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.DESCRICAO_MATERIA_MAX;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.NOME_MATERIA_MAX;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.NOME_MATERIA_MIN;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.PADRAO_TEXTO_COM_CONTEUDO;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.TextoUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Informe o nome da matéria.")
    @Size(min = NOME_MATERIA_MIN, max = NOME_MATERIA_MAX, message = "O nome da matéria deve ter entre {min} e {max} caracteres.")
    @Pattern(regexp = PADRAO_TEXTO_COM_CONTEUDO, message = "O nome da matéria deve ter pelo menos uma letra ou número.")
    private String nome;

    @Size(max = DESCRICAO_MATERIA_MAX, message = "A descrição deve ter no máximo {max} caracteres.")
    private String descricao;

    public void setNome(String nome) {
        this.nome = TextoUtil.normalizar(nome);
    }

    public void setDescricao(String descricao) {
        this.descricao = TextoUtil.aparar(descricao);
    }
}
