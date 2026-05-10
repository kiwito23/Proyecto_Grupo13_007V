package com.TiendaRopa.ms_usuarios.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 150, message = "El nombre de usuario no puede exceder los 150 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 150, message = "El apellido no puede exceder los 150 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 255, message = "La contraseña no puede exceder los 255 caracteres")
    private String contraseña;

    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telefono;

    @Size (max = 255, message = "La dirección no puede exceder los 255 caracteres")
    private String direccion;

}
