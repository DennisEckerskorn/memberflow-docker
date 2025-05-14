import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const PaymentForm = () => {
  const [students, setStudents] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState("");
  const [invoices, setInvoices] = useState([]);
  const [selectedInvoiceId, setSelectedInvoiceId] = useState("");
  const [amount, setAmount] = useState("");
  const [paymentMethod, setPaymentMethod] = useState("CASH");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    api.get("/students/getAll").then((res) => setStudents(res.data));
  }, []);

  const fetchInvoices = async (userId) => {
    try {
      const res = await api.get(`/invoices/getAllInvoicesByUserId/${userId}`);
      const notPaid = res.data.filter((invoice) => invoice.status === "NOT_PAID");
      setInvoices(notPaid);
    } catch (err) {
      console.error(err);
      setError("Error al cargar facturas.");
    }
  };

  const handleStudentChange = (e) => {
    const userId = e.target.value;
    setSelectedUserId(userId);
    setSelectedInvoiceId("");
    setInvoices([]);
    if (userId) {
      fetchInvoices(userId);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!selectedInvoiceId || !amount || !paymentMethod) {
      setError("Completa todos los campos.");
      return;
    }

    try {
      await api.post("/payments/create", {
        invoiceId: parseInt(selectedInvoiceId),
        paymentDate: new Date().toISOString(),
        amount: parseFloat(amount),
        paymentMethod,
        status: "PAID",
      });

      setSuccess("✅ Pago registrado correctamente.");
      setSelectedInvoiceId("");
      setAmount("");
      setPaymentMethod("CASH");
      fetchInvoices(selectedUserId); // actualizar facturas pendientes
    } catch (err) {
      console.error(err);
      setError("❌ Error al registrar el pago.");
    }
  };

  return (
    <div className="content-area">
      <div className="card">
        <h2>Registrar Pago</h2>

        <label>Seleccionar estudiante:</label>
        <select className="form-select" value={selectedUserId} onChange={handleStudentChange}>
          <option value="">-- Selecciona un estudiante --</option>
          {students.map((s) =>
            s.user ? (
              <option key={s.id} value={s.user.id}>
                {s.user.name} {s.user.lastName} ({s.user.email})
              </option>
            ) : null
          )}
        </select>

        {invoices.length > 0 && (
          <>
            <label>Seleccionar factura:</label>
            <select
              className="form-select"
              value={selectedInvoiceId}
              onChange={(e) => setSelectedInvoiceId(e.target.value)}
            >
              <option value="">-- Selecciona una factura --</option>
              {invoices.map((inv) => (
                <option key={inv.id} value={inv.id}>
                  #{inv.id} - {new Date(inv.date).toLocaleDateString()} - Total: {inv.total.toFixed(2)} €
                </option>
              ))}
            </select>
          </>
        )}

        {selectedInvoiceId && (
          <>
            <label>Importe pagado (€):</label>
            <input
              className="form-input"
              type="number"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
            />

            <label>Método de pago:</label>
            <select
              className="form-select"
              value={paymentMethod}
              onChange={(e) => setPaymentMethod(e.target.value)}
            >
              <option value="CASH">Efectivo</option>
              <option value="CARD">Tarjeta</option>
              <option value="TRANSFER">Transferencia</option>
              <option value="BIZUM">Bizum</option>
            </select>

            <ErrorMessage message={error} type="error" />
            <ErrorMessage message={success} type="success" />

            <button className="btn btn-primary" style={{ marginTop: "1rem" }} onClick={handleSubmit}>
              💳 Confirmar Pago
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default PaymentForm;
