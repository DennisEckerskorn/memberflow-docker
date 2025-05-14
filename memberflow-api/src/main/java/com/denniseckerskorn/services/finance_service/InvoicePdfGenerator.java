package com.denniseckerskorn.services.finance_service;

import com.denniseckerskorn.entities.finance.Invoice;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class InvoicePdfGenerator {

    public byte[] generateInvoicePdf(Invoice invoice) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        doc.add(new Paragraph("Factura #" + invoice.getId()));
        doc.add(new Paragraph("Fecha: " + invoice.getDate()));
        doc.add(new Paragraph("Cliente: " + invoice.getUser().getName() + " " + invoice.getUser().getSurname()));
        doc.add(new Paragraph("Estado: " + invoice.getStatus()));

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("Detalle de productos:"));

        invoice.getInvoiceLines().forEach(line -> {
            String product = line.getProductService().getName();
            int qty = line.getQuantity();
            var price = line.getUnitPrice();
            var total = line.getSubtotal();
            doc.add(new Paragraph("- " + product + " x" + qty + " - " + price + " € = " + total + " €"));
        });

        doc.add(new Paragraph(" "));
        doc.add(new Paragraph("TOTAL: " + invoice.getTotal() + " €"));

        doc.close();
        return out.toByteArray();
    }
}
