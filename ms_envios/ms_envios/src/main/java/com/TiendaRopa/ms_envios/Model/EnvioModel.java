package com.TiendaRopa.ms_envios.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "envios")
@Schema(description = "Entidad que representa un envio")
public class EnvioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID del envio", example = "1")
    private Long id;

    @NotNull(message = "El pedidoId es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del pedido asociado", example = "1")
    private Long pedidoId;

    @NotNull(message = "El usuarioId es obligatorio")
    @Column(nullable = false)
    @Schema(description = "ID del usuario", example = "1")
    private Long usuarioId;

    @NotBlank(message = "La direccion es obligatoria")
    @Column(nullable = false)
    @Schema(description = "Dirección de entrega", example = "Av. Principal 123")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Column(nullable = false)
    @Schema(description = "Ciudad de entrega", example = "Santiago")
    private String ciudad;

    @Column(nullable = false)
    @Schema(description = "Estado del envio", example = "PREPARANDO")
    private String estado = "PREPARANDO";

    @Schema(description = "Número de seguimiento del envio", example = "ABC12345")
    private String numeroSeguimiento;
}