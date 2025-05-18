package com.example.virtualShop.dto;

import com.example.virtualShop.entidades.Factura;

import java.util.List;
import java.util.stream.Collectors;

public class FacturaMapper {

    public static FacturaDto toDto(Factura factura) {
        return new FacturaDto(
                factura.getId(),
                factura.getEmisorNombre(),
                factura.getEmisorNif(),
                factura.getEmisorDireccion(),
                factura.getReceptor() != null ? factura.getReceptor().getNombre() : null,
                factura.getReceptor() != null ? factura.getReceptor().getApellido() : null,
                factura.getReceptor() != null ? factura.getReceptor().getCorreo() : null,
                factura.getReceptor() != null ? factura.getReceptor().getTelefono() : null,
                factura.getFechaEmision(),
                factura.getNumeroFactura(),
                factura.getSubtotal(),
                factura.getImpuestos(),
                factura.getTotal(),
                factura.getFormaDePago(),
                factura.getCondicionesVenta(),
                factura.getItems() != null ?
                        factura.getItems().stream()
                                .map(item -> new ItemFacturaDto(
                                        item.getId(),
                                        item.getDescripcionProducto(),
                                        item.getCantidad(),
                                        item.getPrecioUnitario(),
                                        item.getTotal()
                                )).collect(Collectors.toList())
                        : List.of()
        );
    }
}
