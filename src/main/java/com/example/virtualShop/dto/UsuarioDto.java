package com.example.virtualShop.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import java.time.LocalDate;



public record UsuarioDto(@NotBlank String nombre,
                         @NotBlank String apellido,
                         @NotBlank String telefono,
                         @NotBlank String correo,
                         @NotBlank String contrasena,
                         @NotBlank String direccion,
                         @NotBlank LocalDate fechaNacimiento
) {
}
