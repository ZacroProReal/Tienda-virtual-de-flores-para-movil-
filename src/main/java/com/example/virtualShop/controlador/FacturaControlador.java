package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.FacturaDto;
import com.example.virtualShop.dto.FacturaMapper;
import com.example.virtualShop.entidades.Factura;
import com.example.virtualShop.entidades.Usuario;
import com.example.virtualShop.servicios.AutenticacionServicio;
import com.example.virtualShop.servicios.FacturaServicio;
import com.example.virtualShop.servicios.PdfServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@AllArgsConstructor
@RestController
@RequestMapping("/Facturacion")
@CrossOrigin(origins = "*")
@Validated
public class FacturaControlador {

    private final AutenticacionServicio autenticacionServicio;
    private final FacturaServicio facturaServicio;
    private final PdfServicio pdfServicio;

    @PostMapping("/generar")
    public ResponseEntity<?> generarFactura(@RequestHeader("Authorization") String token) {
        try {
            Usuario usuario = autenticacionServicio.obtenerUsuarioDesdeToken(token);
            Factura factura = facturaServicio.generarFacturaDesdeCarrito(usuario);
            FacturaDto facturaDto = FacturaMapper.toDto(factura);
            return ResponseEntity.ok(facturaDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar factura: " + e.getMessage());
        }
    }

    @GetMapping(value = "/obtener", produces = "application/pdf")
    public ResponseEntity<byte[]> generarFacturaEnPdf(@RequestHeader("Authorization") String token) {
        try {
            Usuario usuario = autenticacionServicio.obtenerUsuarioDesdeToken(token);

            // Obtener la última factura generada para el usuario
            Factura factura = facturaServicio.obtenerUltimaFacturaPorUsuario(usuario);
            if (factura == null) {
                return ResponseEntity.badRequest()
                        .body(("No se encontró factura para el usuario").getBytes());
            }

            FacturaDto facturaDto = FacturaMapper.toDto(factura);
            ByteArrayInputStream pdfStream = pdfServicio.generarFacturaPdf(facturaDto);
            byte[] pdfBytes = pdfStream.readAllBytes();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=factura.pdf")
                    .header("Content-Type", "application/pdf")
                    .header("Content-Length", String.valueOf(pdfBytes.length))
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(("Error al generar factura: " + e.getMessage()).getBytes());
        }
    }
}
