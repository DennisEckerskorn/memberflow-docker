import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";

const IVATypeManager = () => {
  const [ivaTypes, setIvaTypes] = useState([]);
  const [newIva, setNewIva] = useState({ percentage: "", description: "" });
  const [error, setError] = useState("");

  const fetchIvaTypes = async () => {
    try {
      const res = await api.get("/iva-types/getAll");
      setIvaTypes(res.data);
    } catch (err) {
      console.error(err);
      setError("Error al obtener los tipos de IVA.");
    }
  };

  useEffect(() => {
    fetchIvaTypes();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewIva((prev) => ({ ...prev, [name]: value }));
  };

  const handleAddIva = async () => {
    setError("");
    if (!newIva.percentage || isNaN(newIva.percentage)) {
      setError("Introduce un porcentaje válido.");
      return;
    }

    try {
      const payload = {
        percentage: parseFloat(newIva.percentage),
        description: newIva.description
      };

      await api.post("/iva-types/create", payload);
      setNewIva({ percentage: "", description: "" });
      fetchIvaTypes();
    } catch (err) {
      console.error(err);
      setError("No se pudo crear el tipo de IVA.");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Seguro que deseas eliminar este tipo de IVA?")) return;
    try {
      await api.delete(`/iva-types/deleteById/${id}`);
      fetchIvaTypes();
    } catch (err) {
      console.error(err);
      setError("No se pudo eliminar el tipo de IVA.");
    }
  };

  return (
    <div className="content-area">
      <h2>Gestión de Tipos de IVA</h2>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <div className="form-section">
        <label>Porcentaje:</label>
        <input
          type="number"
          name="percentage"
          value={newIva.percentage}
          onChange={handleChange}
          step="0.01"
        />

        <label>Descripción:</label>
        <input
          type="text"
          name="description"
          value={newIva.description}
          onChange={handleChange}
        />

        <button className="btn btn-primary" onClick={handleAddIva}>
          + Añadir IVA
        </button>
      </div>

      <table className="table" style={{ marginTop: "2rem" }}>
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
                <button className="btn btn-danger btn-sm" onClick={() => handleDelete(iva.id)}>
                  🗑️ Eliminar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default IVATypeManager;
