package com.denniseckerskorn.services.finance_service;

import com.denniseckerskorn.entities.finance.Invoice;
import com.denniseckerskorn.entities.finance.InvoiceLine;
import com.denniseckerskorn.entities.finance.ProductService;
import com.denniseckerskorn.entities.user_managment.users.Student;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

/**
 * InvoicePdfGenerator is a service that generates a PDF representation of an Invoice.
 * It uses iText library to create the PDF document with invoice details.
 */
@Service
public class InvoicePdfGenerator {

    public byte[] generateInvoicePdf(Invoice invoice) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Document title and metadata
        doc.add(new Paragraph("FACTURA #" + invoice.getId()).setFontSize(16).setBold());
        doc.add(new Paragraph("Fecha: " + invoice.getDate()));
        doc.add(new Paragraph("Estado: " + invoice.getStatus()));
        doc.add(new Paragraph(" "));

        // Cliente information
        var user = invoice.getUser();
        doc.add(new Paragraph("Cliente: " + user.getName() + " " + user.getSurname()));
        doc.add(new Paragraph("Email: " + user.getEmail()));
        doc.add(new Paragraph("Teléfono: " + user.getPhoneNumber()));
        doc.add(new Paragraph("Dirección: " + user.getAddress()));

        // Student information
        Student student = user.getStudent();
        if (student != null) {
            doc.add(new Paragraph("DNI: " + student.getDni()));
            doc.add(new Paragraph("Fecha de nacimiento: " + student.getBirthdate()));
        }

        doc.add(new Paragraph(" "));

        // Products and services details
        doc.add(new Paragraph("Detalle de productos:").setBold());
        doc.add(new Paragraph("Producto | Cant. | Precio | IVA % | Subtotal (sin IVA) | IVA"));

        BigDecimal totalSubtotal = BigDecimal.ZERO;
        BigDecimal totalIVA = BigDecimal.ZERO;

        for (InvoiceLine line : invoice.getInvoiceLines()) {
            ProductService product = line.getProductService();
            String nombre = product.getName();
            int cantidad = line.getQuantity();
            BigDecimal unitPrice = line.getUnitPrice(); // sin IVA
            BigDecimal subtotalSinIVA = unitPrice.multiply(BigDecimal.valueOf(cantidad));
            BigDecimal porcentajeIVA = product.getIvaType().getPercentage();
            BigDecimal iva = subtotalSinIVA.multiply(porcentajeIVA).divide(BigDecimal.valueOf(100));

            totalSubtotal = totalSubtotal.add(subtotalSinIVA);
            totalIVA = totalIVA.add(iva);

            doc.add(new Paragraph(
                    nombre + " | " +
                            cantidad + " | " +
                            unitPrice + " € | " +
                            porcentajeIVA + "% | " +
                            subtotalSinIVA + " € | " +
                            iva + " €"
            ));
        }

        // Totals
        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Subtotal (sin IVA): " + totalSubtotal + " €").setBold());
        doc.add(new Paragraph("IVA total: " + totalIVA + " €").setBold());
        doc.add(new Paragraph("TOTAL: " + totalSubtotal.add(totalIVA) + " €").setFontSize(14).setBold());

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Gracias por su compra.").setItalic());

        doc.close();
        return out.toByteArray();
    }
}
