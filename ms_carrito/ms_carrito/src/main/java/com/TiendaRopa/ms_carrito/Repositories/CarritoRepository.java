package com.TiendaRopa.ms_carrito.Repositories;

import com.TiendaRopa.ms_carrito.Model.CarritoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<CarritoModel, Long> {
    Optional<CarritoModel> findByUsuarioIdAndActivoTrue(Long usuarioId);
    boolean existsByUsuarioIdAndActivoTrue(Long usuarioId);

}
