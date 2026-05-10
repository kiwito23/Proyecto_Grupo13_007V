package com.TiendaRopa.ms_inventario.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "inventarios")
@Data

public class InventarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "producto_id", nullable = false, unique = true)
    private Long productoId;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column (name = "stock_minimo", nullable = false)
    private Integer stockMinimo;

    @Column(name = "ultima_actualizacion", nullable = false)
    private LocalDateTime ultimaActualizacion;

    @OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimientosInventarioModel> movimientos;

}
