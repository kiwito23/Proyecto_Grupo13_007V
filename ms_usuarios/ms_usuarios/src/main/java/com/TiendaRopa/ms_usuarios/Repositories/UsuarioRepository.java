package com.TiendaRopa.ms_usuarios.Repositories;

import com.TiendaRopa.ms_usuarios.Model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    boolean existsByEmail(String email);
    Optional<UsuarioModel> findByEmail(String email);
    java.util.List<UsuarioModel> findByActivoTrue();

}
