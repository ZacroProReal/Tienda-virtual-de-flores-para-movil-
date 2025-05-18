package com.example.virtualShop.controlador;

import com.example.virtualShop.dto.FacturaDto;
import com.example.virtualShop.dto.FacturaMapper;
import com.example.virtualShop.entidades.Factura;
import com.example.virtualShop.servicios.AutenticacionServicio;
import com.example.virtualShop.servicios.FacturaServicio;
import com.example.virtualShop.servicios.PdfServicio;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
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
    public ResponseEntity<?> generarFactura(@RequestParam Long usuarioId, @RequestParam Long carritoId) {
        try {
            Factura factura = facturaServicio.generarFacturaDesdeCarrito(usuarioId, carritoId);
            FacturaDto facturaDto = FacturaMapper.toDto(factura);
            return ResponseEntity.ok(facturaDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al generar factura: " + e.getMessage());
        }
    }
    @GetMapping(value = "/obtener", produces = "application/pdf")
    public ResponseEntity<byte[]> generarFacturaEnPdf(@RequestParam Long usuarioId, @RequestParam Long carritoId) {
        try {
            Factura factura = facturaServicio.generarFacturaDesdeCarrito(usuarioId, carritoId);
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
