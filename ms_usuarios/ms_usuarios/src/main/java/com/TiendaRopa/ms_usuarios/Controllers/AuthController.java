package com.TiendaRopa.ms_usuarios.Controllers;

import com.TiendaRopa.ms_usuarios.DTO.LoginRequestDTO;
import com.TiendaRopa.ms_usuarios.DTO.LoginResponseDTO;
import com.TiendaRopa.ms_usuarios.Model.UsuarioModel;
import com.TiendaRopa.ms_usuarios.Repositories.UsuarioRepository;
import com.TiendaRopa.ms_usuarios.Security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Intento de login para email: {}", request.getEmail());

        // Busca el usuario por email
        UsuarioModel usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElse(null);

        // Valida que exista, esté activo y la contraseña coincida
        if (usuario == null || !usuario.getActivo() ||
                !usuario.getContraseña().equals(request.getContrasena())) {
            log.warn("Login fallido para email: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generarToken(usuario.getEmail());
        log.info("Login exitoso para email: {}", request.getEmail());
        return ResponseEntity.ok(new LoginResponseDTO(token, "Bearer"));
    }
}
