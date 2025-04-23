package com.example.virtualShop.entidades;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table (name = "item_carrito")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ItemCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int cantidad;

    @JsonIgnore
    @ManyToOne
    private Carrito carrito;

    @ManyToOne
    private Producto producto;
}
