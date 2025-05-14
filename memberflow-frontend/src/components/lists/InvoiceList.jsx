import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const InvoiceList = () => {
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchInvoices();
  }, []);

  const fetchInvoices = async () => {
    try {
      const res = await api.get("/invoices/getAll");
      setInvoices(res.data);
      setErrorMsg("");
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al cargar las facturas.");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Estás seguro de que deseas eliminar esta factura?")) return;

    try {
      await api.delete(`/invoices/deleteById/${id}`);
      setInvoices(invoices.filter((inv) => inv.id !== id));
      setSuccessMsg("✅ Factura eliminada correctamente.");
      setErrorMsg("");
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al eliminar la factura.");
      setSuccessMsg("");
    }
  };

  const downloadPdf = async (id) => {
    try {
      const res = await api.get(`/invoices/generatePDFById/${id}`, {
        responseType: "blob",
      });

      const blob = new Blob([res.data], { type: "application/pdf" });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `factura_${id}.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al descargar el PDF.");
    }
  };

  if (loading) {
    return (
      <div className="card">
        <p>Cargando facturas...</p>
      </div>
    );
  }

  return (
    <div className="card">
      <h2>Listado de Facturas</h2>
      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />

      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Cliente</th>
              <th>Email</th>
              <th>Fecha</th>
              <th>Estado</th>
              <th>Total (€)</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {invoices.map((inv) => (
              <tr key={inv.id}>
                <td>{inv.id}</td>
                <td>{inv.user?.name} {inv.user?.surname}</td>
                <td>{inv.user?.email}</td>
                <td>{new Date(inv.date).toLocaleString()}</td>
                <td>{inv.status}</td>
                <td>{inv.total?.toFixed(2)}</td>
                <td>
                  <button className="edit-btn" onClick={() => downloadPdf(inv.id)}>📄 PDF</button>
                  <button className="delete-btn" onClick={() => handleDelete(inv.id)}>🗑️</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default InvoiceList;
