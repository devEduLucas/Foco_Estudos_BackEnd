package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.ErroResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.ConflitoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.CredenciaisInvalidasException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RecursoNaoEncontradoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RegraNegocioException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException e) {
        return resposta(HttpStatus.NOT_FOUND, e.getMessage(), List.of());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponse> conflito(ConflitoException e) {
        return resposta(HttpStatus.CONFLICT, e.getMessage(), List.of());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroResponse> credenciais(CredenciaisInvalidasException e) {
        return resposta(HttpStatus.UNAUTHORIZED, e.getMessage(), List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException e) {
        List<String> detalhes = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos", detalhes);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regraNegocio(RegraNegocioException e) {
        return resposta(HttpStatus.BAD_REQUEST, e.getMessage(), List.of());
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ErroResponse> requisicaoInvalida(Exception e) {
        return resposta(HttpStatus.BAD_REQUEST, "Requisição inválida", List.of());
    }

    private ResponseEntity<ErroResponse> resposta(HttpStatus status, String mensagem, List<String> detalhes) {
        return ResponseEntity.status(status).body(ErroResponse.of(status.value(), mensagem, detalhes));
    }
}
