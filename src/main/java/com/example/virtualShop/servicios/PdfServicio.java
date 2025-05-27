package com.example.virtualShop.servicios;

import com.example.virtualShop.dto.FacturaDto;
import com.example.virtualShop.dto.ItemFacturaDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfServicio {

    public ByteArrayInputStream generarFacturaPdf(FacturaDto factura) {
        Document documento = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(documento, out);
            documento.open();

            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font textoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            documento.add(new Paragraph("FACTURA ELECTRÓNICA", tituloFont));
            documento.add(new Paragraph("Número de Factura: " + factura.numeroFactura(), textoFont));
            documento.add(new Paragraph("Fecha de Emisión: " + factura.fechaEmision(), textoFont));
            documento.add(Chunk.NEWLINE);

            documento.add(new Paragraph("🧾 Información del Emisor", tituloFont));
            documento.add(new Paragraph("Nombre: " + factura.emisorNombre(), textoFont));
            documento.add(new Paragraph("NIF: " + factura.emisorNif(), textoFont));
            documento.add(new Paragraph("Dirección: " + factura.emisorDireccion(), textoFont));
            documento.add(Chunk.NEWLINE);

            documento.add(new Paragraph("👤 Información del Cliente", tituloFont));
            documento.add(new Paragraph("Nombre: " + factura.receptorNombre() + " " + factura.receptorApellido(), textoFont));
            documento.add(new Paragraph("Correo: " + factura.receptorCorreo(), textoFont));
            documento.add(new Paragraph("Teléfono: " + factura.receptorTelefono(), textoFont));
            documento.add(new Paragraph("Dirección del cliente: " + factura.receptorDireccion(), textoFont));
            documento.add(Chunk.NEWLINE);

            documento.add(new Paragraph("🛒 Detalles de la Compra", tituloFont));
            for (ItemFacturaDto item : factura.items()) {
                documento.add(new Paragraph("Producto: " + item.descripcionProducto(), textoFont));
                documento.add(new Paragraph("Cantidad: " + item.cantidad(), textoFont));
                documento.add(new Paragraph("Precio Unitario: $" + item.precioUnitario(), textoFont));
                documento.add(new Paragraph("Total por Ítem: $" + item.total(), textoFont));
                documento.add(new Paragraph("---------------------------------------"));
            }

            documento.add(Chunk.NEWLINE);
            documento.add(new Paragraph("Subtotal: $" + factura.subtotal(), textoFont));
            documento.add(new Paragraph("Impuestos: $" + factura.impuestos(), textoFont));
            documento.add(new Paragraph("Total a Pagar: $" + factura.total(), textoFont));
            documento.add(new Paragraph("Forma de Pago: " + factura.formaDePago(), textoFont));
            documento.add(new Paragraph("Condiciones de Venta: " + factura.condicionesVenta(), textoFont));

            documento.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
