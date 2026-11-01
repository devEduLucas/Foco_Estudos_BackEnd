package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request;

import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.PADRAO_EMAIL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Informe seu e-mail.")
    @Pattern(regexp = PADRAO_EMAIL, message = "Informe um e-mail válido, como nome@exemplo.com.")
    private String email;

    @NotBlank(message = "Informe sua senha.")
    private String senha;

    public void setEmail(String email) {
        this.email = email == null ? null : email.strip();
    }
}
