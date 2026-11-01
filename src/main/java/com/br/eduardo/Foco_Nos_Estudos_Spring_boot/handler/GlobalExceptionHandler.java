package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.handler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.CampoErro;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.ErroResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.ConflitoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.CredenciaisInvalidasException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RecursoNaoEncontradoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RegraNegocioException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

/** Único ponto que transforma exceções em resposta HTTP: sempre {@link ErroResponse}, em português. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final Pattern ANO_COM_MAIS_DE_4_DIGITOS = Pattern.compile("^[+-]?\\d{5,}-.*");
    private static final String MSG_DATA = "Data inválida. Use o formato AAAA-MM-DDTHH:mm:ss, com ano de 4 dígitos.";

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(RecursoNaoEncontradoException e) {
        return resposta(HttpStatus.NOT_FOUND.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponse> conflito(ConflitoException e) {
        return resposta(HttpStatus.CONFLICT.value(), e.getMessage(), doCampo(e.getCampo(), e.getMessage()));
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroResponse> credenciais(CredenciaisInvalidasException e) {
        return resposta(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), List.of());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regraNegocio(RegraNegocioException e) {
        return resposta(HttpStatus.BAD_REQUEST.value(), e.getMessage(), doCampo(e.getCampo(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException e) {
        // Um erro por campo: o de "obrigatório" tem prioridade, depois o de tamanho, depois os demais.
        Map<String, FieldError> porCampo = new TreeMap<>();
        for (FieldError erro : e.getBindingResult().getFieldErrors()) {
            porCampo.merge(erro.getField(), erro, (a, b) -> prioridade(b) < prioridade(a) ? b : a);
        }
        List<CampoErro> campos = porCampo.values().stream()
                .map(f -> new CampoErro(f.getField(), f.getDefaultMessage()))
                .toList();
        return resposta(HttpStatus.BAD_REQUEST.value(), "Corrija os campos indicados.", campos);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoIlegivel(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException formato) {
            String valor = formato.getValue() == null ? null : formato.getValue().toString();
            return resposta(HttpStatus.BAD_REQUEST.value(), "Corrija os campos indicados.",
                    List.of(new CampoErro(nomeDoCampo(formato), mensagemDeFormato(formato.getTargetType(), valor))));
        }
        if (e.getCause() instanceof MismatchedInputException tipo && !tipo.getPath().isEmpty()) {
            return resposta(HttpStatus.BAD_REQUEST.value(), "Corrija os campos indicados.",
                    List.of(new CampoErro(nomeDoCampo(tipo), mensagemDeFormato(tipo.getTargetType(), null))));
        }
        return resposta(HttpStatus.BAD_REQUEST.value(), "Corpo da requisição inválido. Envie um JSON válido.",
                List.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> parametroInvalido(MethodArgumentTypeMismatchException e) {
        String valor = e.getValue() == null ? null : e.getValue().toString();
        return resposta(HttpStatus.BAD_REQUEST.value(), "Corrija os parâmetros indicados.",
                List.of(new CampoErro(e.getName(), mensagemDeFormato(e.getRequiredType(), valor))));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> parametroAusente(MissingServletRequestParameterException e) {
        return resposta(HttpStatus.BAD_REQUEST.value(), "Corrija os parâmetros indicados.",
                List.of(new CampoErro(e.getParameterName(), "Parâmetro obrigatório.")));
    }

    /** Último recurso do banco (ex.: duas requisições simultâneas passaram pela checagem de duplicidade). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> integridade(DataIntegrityViolationException e) {
        log.warn("Violação de integridade", e);
        return resposta(HttpStatus.CONFLICT.value(),
                "Não foi possível salvar: os dados conflitam com um registro existente.", List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> inesperado(Exception e) {
        if (e instanceof ErrorResponse springError) { // 404 de rota, 405, 415 etc. já trazem o status certo
            int codigo = springError.getStatusCode().value();
            return resposta(codigo, mensagemHttp(codigo), List.of());
        }
        log.error("Erro inesperado", e);
        return resposta(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor. Tente novamente em instantes.", List.of());
    }

    private ResponseEntity<ErroResponse> resposta(int status, String mensagem, List<CampoErro> campos) {
        return ResponseEntity.status(status).body(ErroResponse.of(status, mensagem, campos));
    }

    private List<CampoErro> doCampo(String campo, String mensagem) {
        return campo == null ? List.of() : List.of(new CampoErro(campo, mensagem));
    }

    private int prioridade(FieldError erro) {
        String codigo = erro.getCode();
        if ("NotBlank".equals(codigo) || "NotNull".equals(codigo)) {
            return 0;
        }
        return "Size".equals(codigo) ? 1 : 2;
    }

    private String nomeDoCampo(JsonMappingException e) {
        List<JsonMappingException.Reference> caminho = e.getPath();
        String nome = caminho.isEmpty() ? null : caminho.get(caminho.size() - 1).getFieldName();
        return nome == null ? "corpo" : nome;
    }

    private String mensagemDeFormato(Class<?> tipo, String valor) {
        if (tipo != null && tipo.isEnum()) {
            String aceitos = Arrays.stream(tipo.getEnumConstants()).map(Object::toString)
                    .collect(Collectors.joining(", "));
            return "Valor inválido. Valores aceitos: " + aceitos + ".";
        }
        if (tipo != null && (LocalDateTime.class.isAssignableFrom(tipo) || LocalDate.class.isAssignableFrom(tipo))) {
            if (valor != null && ANO_COM_MAIS_DE_4_DIGITOS.matcher(valor).matches()) {
                return "O ano deve ter no máximo 4 dígitos.";
            }
            return MSG_DATA;
        }
        if (tipo != null && Number.class.isAssignableFrom(tipo)) {
            return "Informe um número válido.";
        }
        return "Valor inválido.";
    }

    private String mensagemHttp(int codigo) {
        return switch (codigo) {
            case 400 -> "Requisição inválida.";
            case 404 -> "Recurso não encontrado.";
            case 405 -> "Método HTTP não permitido para esta rota.";
            case 406 -> "Formato de resposta não suportado.";
            case 415 -> "Tipo de conteúdo não suportado. Envie application/json.";
            default -> "Não foi possível processar a requisição.";
        };
    }
}
