package com.example.virtualShop.entidades;
import jakarta.persistence.*;
import lombok.*;

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
}
