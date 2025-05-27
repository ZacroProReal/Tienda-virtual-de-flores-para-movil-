package com.example.virtualShop.dto;



import java.time.LocalDate;


public record UsuarioPerfilDto( String nombre,
                                String apellido,
                                String correo,
                                String telefono,
                                String direccion,
                                LocalDate fechaNacimiento
) {
}
