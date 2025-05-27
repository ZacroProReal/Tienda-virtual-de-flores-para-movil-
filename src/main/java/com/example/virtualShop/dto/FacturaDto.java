package com.example.virtualShop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FacturaDto(
        Long id,
        String emisorNombre,
        String emisorNif,
        String emisorDireccion,
        String receptorNombre,
        String receptorApellido,
        String receptorCorreo,
        String receptorTelefono,
        String receptorDireccion,
        LocalDateTime fechaEmision,
        String numeroFactura,
        BigDecimal subtotal,
        BigDecimal impuestos,
        BigDecimal total,
        String formaDePago,
        String condicionesVenta,
        List<ItemFacturaDto> items
) {}


