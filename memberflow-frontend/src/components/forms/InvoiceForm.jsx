import React, { useEffect, useState } from "react";
import InvoiceLineItem from "./InvoiceLineItem";
import ErrorMessage from "../common/ErrorMessage";
import api from "../../api/axiosConfig";
import "../styles/ContentArea.css";

const InvoiceForm = () => {
  const [students, setStudents] = useState([]);
  const [userId, setUserId] = useState("");
  const [date, setDate] = useState(new Date().toISOString().slice(0, 16));
  const [lines, setLines] = useState([]);
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");
  const [createdInvoice, setCreatedInvoice] = useState(null);

  useEffect(() => {
    api.get("/students/getAll").then((res) => setStudents(res.data));
    api.get("/products-services/getAll").then((res) => setProducts(res.data));
  }, []);

  const addLine = () => setLines([...lines, { productServiceId: "", quantity: 1 }]);

  const updateLine = (index, updatedLine) => {
    const newLines = [...lines];
    newLines[index] = updatedLine;
    setLines(newLines);
  };

  const removeLine = (index) => {
    setLines(lines.filter((_, i) => i !== index));
  };

  const calculateSubtotal = () =>
    lines.reduce((acc, line) => {
      const product = products.find((p) => p.id === parseInt(line.productServiceId));
      return product ? acc + product.price * (parseInt(line.quantity) || 1) : acc;
    }, 0);

  const calculateIVA = () =>
    lines.reduce((acc, line) => {
      const product = products.find((p) => p.id === parseInt(line.productServiceId));
      if (!product || !product.ivaType) return acc;
      const quantity = parseInt(line.quantity) || 1;
      return acc + (product.price * quantity * (product.ivaType.percentage / 100));
    }, 0);

  const calculateTotal = () => calculateSubtotal() + calculateIVA();

  const createInvoice = async () => {
    setError("");
    setCreatedInvoice(null);

    if (!userId || lines.length === 0) {
      setError("Selecciona un estudiante y al menos un producto.");
      return;
    }

    try {
      const preparedLines = lines.map((line) => {
        const product = products.find((p) => p.id === parseInt(line.productServiceId));
        return {
          productServiceId: parseInt(line.productServiceId),
          quantity: parseInt(line.quantity),
          unitPrice: product.price,
        };
      });

      const payload = {
        userId: parseInt(userId),
        date: new Date(date).toISOString(),
        status: "NOT_PAID",
        total: calculateTotal(),
        lines: preparedLines,
      };

      const res = await api.post("/invoices/createInvoiceWithLines", payload);
      setCreatedInvoice(res.data);
      alert("✅ Factura creada correctamente.");
    } catch (err) {
      setError("❌ Error al crear la factura: " + (err.response?.data?.message || err.message));
    }
  };

  const downloadPdf = async () => {
    try {
      const response = await api.get(`/invoices/generatePDFById/${createdInvoice.id}`, {
        responseType: "blob",
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `factura_${createdInvoice.id}.pdf`);
      document.body.appendChild(link);
      link.click();
    } catch (err) {
      setError("❌ Error al descargar el PDF.");
    }
  };

  return (
    <div className="content-area">
      <div className="card">
        <h2>Crear Factura</h2>

        <div className="form-column">
          <label>Estudiante</label>
          <select className="form-select" value={userId} onChange={(e) => setUserId(e.target.value)} required>
            <option value="">-- Selecciona un estudiante --</option>
            {students.map((s) =>
              s.user ? (
                <option key={s.id} value={s.user.id}>
                  {s.user.name} {s.user.lastName} ({s.user.email})
                </option>
              ) : null
            )}
          </select>

          <label>Fecha de emisión</label>
          <input
            className="form-input"
            type="datetime-local"
            value={date}
            onChange={(e) => setDate(e.target.value)}
            required
          />
        </div>

        <h3>Productos / Servicios</h3>
        {lines.map((line, i) => (
          <InvoiceLineItem
            key={i}
            index={i}
            line={line}
            products={products}
            onUpdate={updateLine}
            onRemove={removeLine}
          />
        ))}

        <div className="invoice-summary">
          <div>
            <h4>Subtotal:</h4>
            <p><strong>{calculateSubtotal().toFixed(2)} €</strong></p>
          </div>
          <div>
            <h4>IVA:</h4>
            <p><strong>{calculateIVA().toFixed(2)} €</strong></p>
          </div>
          <div>
            <h4>Total con IVA:</h4>
            <p><strong>{calculateTotal().toFixed(2)} €</strong></p>
          </div>
        </div>

        <ErrorMessage message={error} />

        <div style={{ display: "flex", gap: "10px", marginTop: "1rem" }}>
          <button className="btn btn-success" onClick={addLine}>+ Añadir Producto</button>
          <button className="btn btn-primary" onClick={createInvoice}>Crear Factura</button>
        </div>

        {createdInvoice && (
          <div style={{ marginTop: "2rem" }}>
            <h4>Factura #{createdInvoice.id} creada correctamente</h4>
            <p><strong>Total:</strong> {createdInvoice.total.toFixed(2)} €</p>
            <button className="btn btn-primary" onClick={downloadPdf}>📄 Descargar PDF</button>
          </div>
        )}
      </div>
    </div>
  );
};

export default InvoiceForm;
