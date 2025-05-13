import React, { useEffect, useState } from "react";
import InvoiceLineItem from "./InvoiceLineItem";
import api from "../../api/axiosConfig";

const InvoiceForm = () => {
  const [students, setStudents] = useState([]);
  const [userId, setUserId] = useState("");
  const [date, setDate] = useState(new Date().toISOString().slice(0, 16));
  const [lines, setLines] = useState([]);
  const [products, setProducts] = useState([]);

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

  const createInvoice = async () => {
    if (!userId || lines.length === 0) {
      alert("Selecciona un estudiante y al menos un producto.");
      return;
    }

    const invoiceRes = await api.post("/invoices/create", {
      user: { id: parseInt(userId) },
      date,
      total: 0,
      status: "NOT_PAID",
      invoiceLineIds: [],
    });

    const invoice = invoiceRes.data;

    for (const line of lines) {
      const product = products.find(
        (p) => p.id === parseInt(line.productServiceId)
      );
      const subtotal = product.price * line.quantity;

      await api.post(`/invoices/addLinesByInvoiceId/${invoice.id}`, {
        productServiceId: parseInt(line.productServiceId),
        quantity: parseInt(line.quantity),
        unitPrice: product.price,
        subtotal,
        description: product.description,
      });
    }

    await api.put(`/invoices/recalculateTotalOfInvoiceById/${invoice.id}`);

    alert("Factura creada correctamente.");
    setUserId("");
    setLines([]);
  };

  return (
    <div className="content-area">
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
    </div>
  );
};

export default InvoiceForm;
