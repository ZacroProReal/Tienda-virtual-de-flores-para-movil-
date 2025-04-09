package com.example.virtualShop.dto;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record ProductoDto (@NotBlank String nombre){
}
