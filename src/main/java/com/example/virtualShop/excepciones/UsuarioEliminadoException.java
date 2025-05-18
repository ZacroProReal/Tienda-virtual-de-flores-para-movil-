package com.example.virtualShop.excepciones;

public class UsuarioEliminadoException extends RuntimeException {
    public UsuarioEliminadoException(String mensaje) {
        super(mensaje);
    }
}
