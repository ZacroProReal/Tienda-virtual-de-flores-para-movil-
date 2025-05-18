package com.example.virtualShop.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "facturas")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Datos del emisor (puedes poner fijos o si quieres, otra entidad Empresa)
    private String emisorNombre;
    private String emisorNif;
    private String emisorDireccion;

    // Datos del receptor (usuario)
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario receptor;

    // Fecha de emisión
    private LocalDateTime fechaEmision;

    // Número de factura
    @Column(unique = true)
    private String numeroFactura;

    private BigDecimal subtotal;
    private BigDecimal impuestos; // IVA total
    private BigDecimal total;

    // Forma de pago y condiciones
    private String formaDePago;
    private String condicionesVenta;

    // Detalles de los ítems facturados
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemFactura> items;
}
