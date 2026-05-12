package com.TiendaRopa.ms_usuarios.Services;

import com.TiendaRopa.ms_usuarios.DTO.UsuarioDTO;
import com.TiendaRopa.ms_usuarios.Model.UsuarioModel;
import com.TiendaRopa.ms_usuarios.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<UsuarioModel>listarUsuariosActivos() {
        log.info("Listando todos los usuarios activos");
        return usuarioRepository.findByActivoTrue();
    }

    public UsuarioModel obtenerUsuarioPorId(Long id) {
        log.info("Obteniendo usuario por ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con ID: {}", id);
                    return new RuntimeException("Usuario no encontrado con ID: " + id);
                });
    }

    public UsuarioModel crearUsuario(UsuarioDTO usuarioDTO) {
        log.info("Creando nuevo usuario con email: {}", usuarioDTO.getEmail());

        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            log.error("Ya existe un usuario con email: {}", usuarioDTO.getEmail());
            throw new RuntimeException("Ya existe un usuario con email: " + usuarioDTO.getEmail());
        }

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setContraseña(usuarioDTO.getContraseña());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setDireccion(usuarioDTO.getDireccion());
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        UsuarioModel usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con id {}", usuarioGuardado.getId());
        return usuarioGuardado;
        
    }

    public UsuarioModel actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        log.info("Actualizando usuario con ID: {}", id);
        UsuarioModel usuarioExistente = obtenerUsuarioPorId(id);
        usuarioExistente.setNombre(usuarioDTO.getNombre());
        usuarioExistente.setApellido(usuarioDTO.getApellido());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setContraseña(usuarioDTO.getContraseña());
        usuarioExistente.setTelefono(usuarioDTO.getTelefono());
        usuarioExistente.setDireccion(usuarioDTO.getDireccion());
        return usuarioRepository.save(usuarioExistente);
    }


    public void desactivarUsuario(Long id) {
        log.info("Desactivando usuario con ID: {}", id);
        UsuarioModel usuarioExistente = obtenerUsuarioPorId(id);
        usuarioExistente.setActivo(false);
        usuarioRepository.save(usuarioExistente);
        log.info("Usuario desactivado con ID: {}", id);
    }
}
