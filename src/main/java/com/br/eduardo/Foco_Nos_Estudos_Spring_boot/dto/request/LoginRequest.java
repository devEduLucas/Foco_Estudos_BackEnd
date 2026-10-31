package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;
}
