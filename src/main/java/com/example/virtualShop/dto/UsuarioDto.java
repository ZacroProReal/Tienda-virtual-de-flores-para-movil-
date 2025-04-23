package com.example.virtualShop.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import java.time.LocalDate;



public record UsuarioDto(@NotBlank String nombre,
                         String apellido,
                         String telefono,
                         String correo,
                         String contrasena,
                         LocalDate fechaNacimiento
) {
}
