import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const MembershipDetails = () => {
  const [memberships, setMemberships] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchMemberships();
  }, []);

  const fetchMemberships = async () => {
    try {
      const res = await api.get("/memberships/getAll");
      setMemberships(res.data);
    } catch (err) {
      setErrorMsg("❌ Error al cargar las membresías");
    }
  };

  const handleInputChange = (id, field, value) => {
    setMemberships((prev) =>
      prev.map((m) => (m.id === id ? { ...m, [field]: value } : m))
    );
  };

  const handleUpdate = async (id) => {
    const membership = memberships.find((m) => m.id === id);
    if (!membership) return;

    try {
      await api.put(`/memberships/update/${id}`, membership);
      setSuccessMsg("✅ Membresía actualizada correctamente");
      setErrorMsg("");
      fetchMemberships();
    } catch (err) {
      console.error(err);
      setSuccessMsg("");
      setErrorMsg("❌ Error al actualizar la membresía");
    }
  };

  return (
    <div className="card">
      <h2>Detalles de Membresías</h2>

      <ErrorMessage type="success" message={successMsg} />
      <ErrorMessage type="error" message={errorMsg} />

      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Tipo</th>
              <th>Inicio</th>
              <th>Fin</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {memberships.map((m) => (
              <tr key={m.id}>
                <td>{m.id}</td>
                <td>{m.type}</td>
                <td>
                  <input
                    type="date"
                    value={m.startDate}
                    onChange={(e) =>
                      handleInputChange(m.id, "startDate", e.target.value)
                    }
                  />
                </td>
                <td>
                  <input
                    type="date"
                    value={m.endDate}
                    onChange={(e) =>
                      handleInputChange(m.id, "endDate", e.target.value)
                    }
                  />
                </td>
                <td>
                  <select
                    value={m.status}
                    onChange={(e) =>
                      handleInputChange(m.id, "status", e.target.value)
                    }
                  >
                    <option value="ACTIVE">Activo</option>
                    <option value="INACTIVE">Inactivo</option>
                    <option value="SUSPENDED">Suspendido / Expirado</option>
                  </select>
                </td>
                <td>
                  <button onClick={() => handleUpdate(m.id)}>Actualizar</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default MembershipDetails;
