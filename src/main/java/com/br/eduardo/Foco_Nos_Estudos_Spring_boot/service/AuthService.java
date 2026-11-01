package com.br.eduardo.Foco_Nos_Estudos_Spring_boot.service;

import static com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.RegrasNegocio.SENHA_MAX_BYTES;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.model.UsuarioEntity;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.database.repository.IUsuarioRepository;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.CadastroRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.request.LoginRequest;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.dto.response.LoginResponse;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.ConflitoException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.CredenciaisInvalidasException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.exception.RegraNegocioException;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.security.JwtService;
import com.br.eduardo.Foco_Nos_Estudos_Spring_boot.util.TextoUtil;

@Service
public class AuthService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponse cadastrar(CadastroRequest req) {
        if (req.getSenha().getBytes(StandardCharsets.UTF_8).length > SENHA_MAX_BYTES) {
            throw new RegraNegocioException("senha",
                    "A senha excede o limite de " + SENHA_MAX_BYTES + " bytes. Use uma senha mais curta.");
        }
        String email = normalizar(req.getEmail());
        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflitoException("email", "Este e-mail já está cadastrado.");
        }
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome(TextoUtil.normalizar(req.getNome()));
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(req.getSenha()));
        usuario = usuarioRepository.save(usuario);
        return resposta(usuario);
    }

    /** A mensagem é a mesma para e-mail inexistente e senha errada, para não revelar quais e-mails têm conta. */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest req) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(normalizar(req.getEmail()))
                .filter(u -> passwordEncoder.matches(req.getSenha(), u.getSenhaHash()))
                .orElseThrow(() -> new CredenciaisInvalidasException("E-mail ou senha inválidos."));
        return resposta(usuario);
    }

    private LoginResponse resposta(UsuarioEntity u) {
        return new LoginResponse(jwtService.gerarToken(u.getId()), "Bearer", u.getId(), u.getNome());
    }

    private String normalizar(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
