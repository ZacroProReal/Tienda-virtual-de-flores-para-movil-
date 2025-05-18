package com.example.virtualShop.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "items_factura")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ItemFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcionProducto;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal total; // precioUnitario * cantidad

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}
