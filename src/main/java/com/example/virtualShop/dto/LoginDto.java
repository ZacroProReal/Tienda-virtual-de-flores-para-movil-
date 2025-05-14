package com.example.virtualShop.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;


public record LoginDto(@NotBlank String correo,
                       @NotBlank String contrasena
) {
}
