package com.TiendaRopa.ms_carrito.Repositories;

import com.TiendaRopa.ms_carrito.Model.CarritoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<CarritoModel, Long> {
    Optional<CarritoModel> findByUsuarioIdAndActivoTrue(Long usuarioId);
    boolean existsByUsuarioIdAndActivoTrue(Long usuarioId);
    Optional<CarritoModel> findByUsuarioId(Long usuarioId);

}
