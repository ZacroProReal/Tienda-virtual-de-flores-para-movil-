package com.example.virtualShop.dto;

import java.math.BigDecimal;

public record ItemFacturaDto(
        Long id,
        String descripcionProducto,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal total
) {
}
