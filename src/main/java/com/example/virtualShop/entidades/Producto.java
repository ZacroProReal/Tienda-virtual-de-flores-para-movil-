package com.example.virtualShop.entidades;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table (name = "docentes")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer cantidadDisponible;
    private String colorFlores;
    private boolean disponibilidad;

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;

}
