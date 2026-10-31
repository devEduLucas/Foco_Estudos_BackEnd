package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.CadastroRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.LoginRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.LoginResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse cadastrar(@Valid @RequestBody CadastroRequest req) {
        return authService.cadastrar(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}
