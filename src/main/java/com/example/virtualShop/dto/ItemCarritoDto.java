package com.example.virtualShop.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;


public record ItemCarritoDto(@NotBlank    Long id,
                             int cantidad,
                             ProductoDto producto){
}
