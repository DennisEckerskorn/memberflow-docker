import React, { useState, useEffect } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import '../styles/ContentArea.css'; 

const PaymentList = () => {
  const [students, setStudents] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState("");
  const [payments, setPayments] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    api.get("/students/getAll").then((res) => setStudents(res.data));
  }, []);

  const fetchPayments = async (userId) => {
    try {
      const res = await api.get(`/payments/getAllByUserId/${userId}`);
      setPayments(res.data);
    } catch (err) {
      console.error(err);
      setError("Error al cargar pagos.");
    }
  };

  const handleStudentChange = (e) => {
    const userId = e.target.value;
    setSelectedUserId(userId);
    if (userId) {
      fetchPayments(userId);
    } else {
      setPayments([]);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Seguro que quieres eliminar este pago?")) {
      try {
        await api.delete(`/payments/deleteById/${id}`);
        fetchPayments(selectedUserId);
      } catch (err) {
        console.error(err);
        setError("Error al eliminar el pago.");
      }
    }
  };

  return (
    <div className="content-area">
      <div className="card">
        <h2>Listado de Pagos</h2>

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

        <ErrorMessage message={error} />

        {payments.length > 0 && (
          <table className="table" style={{ marginTop: "1rem" }}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Factura</th>
                <th>Fecha</th>
                <th>Monto (€)</th>
                <th>Método</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {payments.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>#{p.invoiceId}</td>
                  <td>{new Date(p.paymentDate).toLocaleDateString()}</td>
                  <td>{p.amount.toFixed(2)}</td>
                  <td>{p.paymentMethod}</td>
                  <td>{p.status}</td>
                  <td>
                    {/* Botón editar lo montamos luego */}
                    {/* <button className="btn btn-secondary btn-sm" onClick={() => handleEdit(p)}>✏️</button> */}
                    <button className="btn btn-danger btn-sm" onClick={() => handleDelete(p.id)}>
                      🗑️
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default PaymentList;
