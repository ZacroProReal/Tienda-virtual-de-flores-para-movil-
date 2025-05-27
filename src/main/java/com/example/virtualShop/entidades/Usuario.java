package com.example.virtualShop.entidades;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table (name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    private String nombre;
    private String apellido;
    private String telefono;
    private String contrasena;
    private String direccion;

    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado;

    @Column(unique = true)
    private String correo;

    private LocalDate fechaNacimiento;

    @Builder.Default
    private Integer rol = 2;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Carrito carrito;

}
