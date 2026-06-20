package com.TiendaRopa.ms_usuarios.ServiceTest;

import com.TiendaRopa.ms_usuarios.DTO.UsuarioDTO;
import com.TiendaRopa.ms_usuarios.Exceptions.UsuarioNotFoundException;
import com.TiendaRopa.ms_usuarios.Model.UsuarioModel;
import com.TiendaRopa.ms_usuarios.Repositories.UsuarioRepository;
import com.TiendaRopa.ms_usuarios.Services.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioModel usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setNombre("Katerine");
        usuario.setApellido("Olmedo");
        usuario.setEmail("katerine@tiendaropa.cl");
        usuario.setContraseña("68KatSilv");
        usuario.setTelefono("+56912345678");
        usuario.setDireccion("Av. Principal 123");
        usuario.setActivo(true);
        usuario.setFechaRegistro(LocalDateTime.now());

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Katerine");
        usuarioDTO.setApellido("Olmedo");
        usuarioDTO.setEmail("katerine@tiendaropa.cl");
        usuarioDTO.setContraseña("68KatSilv");
        usuarioDTO.setTelefono("+56912345678");
        usuarioDTO.setDireccion("Av. Principal 123");
    }

    // ----------------------------------------------------------------
    // listarUsuariosActivos()
    // ----------------------------------------------------------------
    @Test
    void testListarUsuariosActivos_retornaListaDeUsuariosActivos() {
        // GIVEN
        when(usuarioRepository.findByActivoTrue()).thenReturn(List.of(usuario));

        // WHEN
        List<UsuarioModel> resultado = usuarioService.listarUsuariosActivos();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
        verify(usuarioRepository, times(1)).findByActivoTrue();
    }

    // ----------------------------------------------------------------
    // obtenerUsuarioPorId() — encontrado
    // ----------------------------------------------------------------
    @Test
    void testObtenerUsuarioPorId_encontrado_retornaUsuario() {
        // GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // WHEN
        UsuarioModel resultado = usuarioService.obtenerUsuarioPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals("Katerine", resultado.getNombre());
        assertEquals("katerine@tiendaropa.cl", resultado.getEmail());
    }

    // ----------------------------------------------------------------
    // obtenerUsuarioPorId() — no encontrado → excepción
    // ----------------------------------------------------------------
    @Test
    void testObtenerUsuarioPorId_noEncontrado_lanzaExcepcion() {
        // GIVEN
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        UsuarioNotFoundException ex = assertThrows(
                UsuarioNotFoundException.class,
                () -> usuarioService.obtenerUsuarioPorId(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
    }

    // ----------------------------------------------------------------
    // crearUsuario() — email nuevo → guarda exitosamente
    // ----------------------------------------------------------------
    @Test
    void testCrearUsuario_emailNuevo_guardaYRetornaUsuario() {
        // GIVEN: el email no existe en BD
        when(usuarioRepository.existsByEmail("katerine@tiendaropa.cl")).thenReturn(false);
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuario);

        // WHEN
        UsuarioModel resultado = usuarioService.crearUsuario(usuarioDTO);

        // THEN: el usuario fue creado y está activo
        assertNotNull(resultado);
        assertEquals("katerine@tiendaropa.cl", resultado.getEmail());
        assertTrue(resultado.getActivo());
        verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
    }

    // ----------------------------------------------------------------
    // crearUsuario() — email duplicado → lanza excepción
    // ----------------------------------------------------------------
    @Test
    void testCrearUsuario_emailDuplicado_lanzaExcepcion() {
        // GIVEN: ya existe un usuario con ese email
        when(usuarioRepository.existsByEmail("katerine@tiendaropa.cl")).thenReturn(true);

        // WHEN / THEN
        assertThrows(
                UsuarioNotFoundException.class,
                () -> usuarioService.crearUsuario(usuarioDTO)
        );
        // No se debe guardar nada si el email ya existe
        verify(usuarioRepository, never()).save(any());
    }

    // ----------------------------------------------------------------
    // desactivarUsuario() — DELETE lógico (activo = false)
    // ----------------------------------------------------------------
    @Test
    void testDesactivarUsuario_ponActivoEnFalseYGuarda() {
        // GIVEN
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuario);

        // WHEN
        usuarioService.desactivarUsuario(1L);

        // THEN: el usuario queda inactivo
        assertFalse(usuario.getActivo());
        verify(usuarioRepository, times(1)).save(usuario);
    }
}

