import React, { useEffect, useState } from "react";
import InvoiceLineItem from "./InvoiceLineItem";
import ErrorMessage from "../common/ErrorMessage";
import api from "../../api/axiosConfig";

const InvoiceForm = () => {
  const [students, setStudents] = useState([]);
  const [userId, setUserId] = useState("");
  const [date, setDate] = useState(new Date().toISOString().slice(0, 16));
  const [lines, setLines] = useState([]);
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");
  const [createdInvoiceId, setCreatedInvoiceId] = useState(null);

  useEffect(() => {
    api.get("/students/getAll").then((res) => setStudents(res.data));
    api.get("/products-services/getAll").then((res) => setProducts(res.data));
  }, []);

  const addLine = () => {
    setLines([...lines, { productServiceId: "", quantity: 1 }]);
  };

  const updateLine = (index, updatedLine) => {
    const newLines = [...lines];
    newLines[index] = updatedLine;
    setLines(newLines);
  };

  const removeLine = (index) => {
    setLines(lines.filter((_, i) => i !== index));
  };

  const calculateTotal = () => {
    return lines.reduce((acc, line) => {
      const product = products.find(
        (p) => p.id === parseInt(line.productServiceId)
      );
      if (!product) return acc;
      return acc + product.price * line.quantity;
    }, 0);
  };

  const createInvoice = async () => {
    setError("");
    setCreatedInvoiceId(null);

    if (!userId || lines.length === 0) {
      setError("Selecciona un estudiante y al menos un producto.");
      return;
    }

    try {
      const preparedLines = lines.map((line) => {
        const product = products.find(
          (p) => p.id === parseInt(line.productServiceId)
        );
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
      const invoiceId = res.data.id;

      setCreatedInvoiceId(invoiceId);
      alert("✅ Factura creada correctamente.");
    } catch (err) {
      console.error(err);
      setError("❌ Error al crear la factura: " + (err.response?.data?.message || err.message));
    }
  };

  const downloadPdf = async () => {
    try {
      const response = await api.get(`/invoices/generatePDFById/${createdInvoiceId}`, {
        responseType: "blob", // para recibir el PDF correctamente
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `factura_${createdInvoiceId}.pdf`);
      document.body.appendChild(link);
      link.click();
    } catch (err) {
      console.error(err);
      setError("❌ Error al descargar el PDF.");
    }
  };

  return (
    <div className="content-area">
      <div className="card">
        <h2>Crear Factura</h2>

        <label>Seleccionar estudiante:</label>
        <select
          className="form-select"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
        >
          <option value="">-- Selecciona un estudiante --</option>
          {students.map((s) =>
            s.user ? (
              <option key={s.id} value={s.user.id}>
                {s.user.name} {s.user.lastName} ({s.user.email})
              </option>
            ) : null
          )}
        </select>

        <label>Fecha de emisión:</label>
        <input
          className="form-input"
          type="datetime-local"
          value={date}
          onChange={(e) => setDate(e.target.value)}
        />

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

        <h4 style={{ marginTop: "1rem" }}>
          Total: <strong>{calculateTotal().toFixed(2)} €</strong>
        </h4>

        <ErrorMessage message={error} />

        <div style={{ marginTop: "1rem" }}>
          <button className="btn btn-secondary" onClick={addLine}>
            + Añadir Producto
          </button>
          <button
            className="btn btn-primary"
            onClick={createInvoice}
            style={{ marginLeft: "1rem" }}
          >
            ✅ Crear Factura
          </button>
        </div>

        {createdInvoiceId && (
          <div style={{ marginTop: "2rem" }}>
            <h4>Factura creada: #{createdInvoiceId}</h4>
            <button className="btn btn-primary" onClick={downloadPdf}>
              📄 Descargar PDF
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default InvoiceForm;
