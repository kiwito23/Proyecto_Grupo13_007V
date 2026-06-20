package com.TiendaRopa.ms_usuarios.Controllers;

import com.TiendaRopa.ms_usuarios.DTO.UsuarioDTO;
import com.TiendaRopa.ms_usuarios.Model.UsuarioModel;
import com.TiendaRopa.ms_usuarios.Services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar usuarios activos",
        description = "Devuelve los usuarios que no han sido desactivados")
    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarActivos() {
        return ResponseEntity.ok(usuarioService.listarUsuariosActivos());
    }

    @Operation(summary = "Obtener usuario por ID",
        description = "Devuelve el detalle de un usuario específico")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioModel> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    @Operation(summary = "Registrar usuario",
        description = "Crea un nuevo usuario en el sistema, validando que el email no esté duplicado")
    @PostMapping
    public ResponseEntity<UsuarioModel> crear(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(dto));
    }

    @Operation(summary = "Actualizar usuario",
        description = "Modifica los datos de un usuario existente")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioModel> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    @Operation(summary = "Desactivar usuario",
        description = "Realiza un borrado lógico del usuario en vez de eliminarlo físicamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}