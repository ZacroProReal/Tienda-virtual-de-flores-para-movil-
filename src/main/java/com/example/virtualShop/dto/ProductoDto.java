package com.example.virtualShop.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;


import java.math.BigDecimal;

public record ProductoDto (@NotBlank String nombre,
                           String descripcion,
                           BigDecimal precio,
                           Integer cantidadDisponible,
                           String colorFlores,
                           boolean disponibilidad,
                           byte[] imagen){
}
