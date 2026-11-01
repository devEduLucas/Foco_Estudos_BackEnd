package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request;

import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.EMAIL_MAX;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.NOME_USUARIO_MAX;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.NOME_USUARIO_MIN;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.PADRAO_EMAIL;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.PADRAO_NOME_PESSOA;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.PADRAO_SENHA;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.SENHA_MAX;
import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.SENHA_MIN;

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
public class CadastroRequest {

    @NotBlank(message = "Informe seu nome completo.")
    @Size(min = NOME_USUARIO_MIN, max = NOME_USUARIO_MAX, message = "O nome deve ter entre {min} e {max} caracteres.")
    @Pattern(regexp = PADRAO_NOME_PESSOA, message = "O nome deve ter apenas letras, espaços, hífen, apóstrofo ou ponto.")
    private String nome;

    @NotBlank(message = "Informe seu e-mail.")
    @Size(max = EMAIL_MAX, message = "O e-mail deve ter no máximo {max} caracteres.")
    @Pattern(regexp = PADRAO_EMAIL, message = "Informe um e-mail válido, como nome@exemplo.com.")
    private String email;

    // 72 = limite do BCrypt
    @NotBlank(message = "Crie uma senha.")
    @Size(min = SENHA_MIN, max = SENHA_MAX, message = "A senha deve ter entre {min} e {max} caracteres.")
    @Pattern(regexp = PADRAO_SENHA, message = "A senha deve ter pelo menos uma letra e um número.")
    private String senha;

    public void setNome(String nome) {
        this.nome = TextoUtil.normalizar(nome);
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.strip();
    }
}
