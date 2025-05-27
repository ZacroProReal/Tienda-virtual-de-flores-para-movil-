package com.example.virtualShop.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "carrito")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference  // Evita la serialización recursiva
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private EstadoCarrito estado;

    private Integer cantidadGeneralProduc;
    private Integer costoGenearl;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private List<ItemCarrito> items = new ArrayList<>();
}
