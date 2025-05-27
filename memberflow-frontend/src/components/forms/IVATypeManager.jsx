import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const IVATypeManager = () => {
  const [ivaTypes, setIvaTypes] = useState([]);
  const [newIva, setNewIva] = useState({ percentage: "", description: "" });
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchIvaTypes();
  }, []);

  const fetchIvaTypes = async () => {
    try {
      const res = await api.get("/iva-types/getAll");
      setIvaTypes(res.data);
      setErrorMsg("");
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al obtener los tipos de IVA.");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewIva((prev) => ({ ...prev, [name]: value }));
  };

  const handleAddIva = async () => {
    setErrorMsg("");
    setSuccessMsg("");
    if (!newIva.percentage || isNaN(newIva.percentage)) {
      setErrorMsg("❌ Introduce un porcentaje válido.");
      return;
    }

    try {
      const payload = {
        percentage: parseFloat(newIva.percentage),
        description: newIva.description
      };

      await api.post("/iva-types/create", payload);
      setNewIva({ percentage: "", description: "" });
      setSuccessMsg("✅ Tipo de IVA añadido correctamente.");
      fetchIvaTypes();
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ No se pudo crear el tipo de IVA.");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Seguro que deseas eliminar este tipo de IVA?")) return;
    try {
      await api.delete(`/iva-types/deleteById/${id}`);
      setSuccessMsg("✅ Tipo de IVA eliminado correctamente.");
      fetchIvaTypes();
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ No se pudo eliminar el tipo de IVA.");
    }
  };

  return (
    <div className="card">
      <h2>Gestión de Tipos de IVA</h2>

      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />

      <div className="form-inline" style={{ marginBottom: "1.5rem" }}>
        <input
          type="number"
          name="percentage"
          placeholder="Porcentaje (%)"
          value={newIva.percentage}
          onChange={handleChange}
          step="0.01"
          className="form-input"
        />
        <input
          type="text"
          name="description"
          placeholder="Descripción"
          value={newIva.description}
          onChange={handleChange}
          className="form-input"
        />
        <button className="btn btn-primary" onClick={handleAddIva}>
          + Añadir IVA
        </button>
      </div>

      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Porcentaje</th>
              <th>Descripción</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {ivaTypes.map((iva) => (
              <tr key={iva.id}>
                <td>{iva.id}</td>
                <td>{iva.percentage}%</td>
                <td>{iva.description}</td>
                <td>
                  <button className="delete-btn" onClick={() => handleDelete(iva.id)}>
                    🗑️
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default IVATypeManager;
