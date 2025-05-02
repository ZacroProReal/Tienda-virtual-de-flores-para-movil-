package com.example.virtualShop.entidades;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(unique = true)
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String contrasena;
    private LocalDate fechaNacimiento;

    @Builder.Default
    private Integer rol = 2;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Carrito carrito;

}
