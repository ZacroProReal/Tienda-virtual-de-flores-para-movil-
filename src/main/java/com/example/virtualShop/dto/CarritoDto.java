package com.example.virtualShop.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;


public record CarritoDto(@NotBlank Long id,
                         LocalDateTime fechaCreacion,
                         String estado,
                         Integer cantidadGeneralProduc,
                         Integer costoGenearl,
                         List<ItemCarritoDto> items
) {
}
