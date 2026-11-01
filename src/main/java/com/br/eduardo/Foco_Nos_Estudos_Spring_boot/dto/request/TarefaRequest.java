package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request;

import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.DESCRICAO_TAREFA_MAX;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.PADRAO_TEXTO_COM_CONTEUDO;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.TITULO_TAREFA_MAX;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.TITULO_TAREFA_MIN;

import java.time.LocalDateTime;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.PrioridadeTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.enums.StatusTarefa;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.TextoUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TarefaRequest {

    @NotBlank(message = "Informe o título da tarefa.")
    @Size(min = TITULO_TAREFA_MIN, max = TITULO_TAREFA_MAX, message = "O título deve ter entre {min} e {max} caracteres.")
    @Pattern(regexp = PADRAO_TEXTO_COM_CONTEUDO, message = "O título deve ter pelo menos uma letra ou número.")
    private String titulo;

    @Size(max = DESCRICAO_TAREFA_MAX, message = "A descrição deve ter no máximo {max} caracteres.")
    private String descricao;

    /** ISO 8601, ex.: 2026-10-12T18:00:00. O ano precisa ter 4 dígitos. */
    @NotNull(message = "Informe o prazo da tarefa.")
    private LocalDateTime dataPrazo;

    @NotNull(message = "Informe o status da tarefa.")
    private StatusTarefa status;

    @NotNull(message = "Informe a prioridade da tarefa.")
    private PrioridadeTarefa prioridade;

    @NotNull(message = "Selecione a matéria da tarefa.")
    @Positive(message = "Selecione uma matéria válida.")
    private Long materiaId;

    public void setTitulo(String titulo) {
        this.titulo = TextoUtil.normalizar(titulo);
    }

    public void setDescricao(String descricao) {
        this.descricao = TextoUtil.aparar(descricao);
    }
}
