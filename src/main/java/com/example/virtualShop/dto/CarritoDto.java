package com.example.virtualShop.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;


public record CarritoDto(@NotBlank Long id,
                         LocalDateTime fechaCreacion,
                         String estado,
                         Integer cantidadGeneralProduc
) {
}
