import React, { useState, useEffect } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";

const ProductForm = ({ onProductAdded }) => {
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [type, setType] = useState("PRODUCT");
  const [status, setStatus] = useState("ACTIVE");
  const [ivaTypes, setIvaTypes] = useState([]);
  const [selectedIvaId, setSelectedIvaId] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    api.get("/iva-types/getAll").then((res) => setIvaTypes(res.data));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!name || !price || !selectedIvaId || !type || !status) {
      setError("Todos los campos son obligatorios.");
      return;
    }

    const ivaTypeIdParsed = parseInt(selectedIvaId);
    if (isNaN(ivaTypeIdParsed)) {
      setError("Debes seleccionar un tipo de IVA válido.");
      return;
    }

    try {
      await api.post("/products-services/create", {
        name,
        price: parseFloat(price),
        ivaTypeId: ivaTypeIdParsed,
        type,
        status,
      });

      setSuccess("✅ Producto/servicio creado correctamente.");
      setName("");
      setPrice("");
      setSelectedIvaId("");
      setType("PRODUCT");
      setStatus("ACTIVE");

      if (onProductAdded) onProductAdded(); // refrescar lista si se usa en conjunto
    } catch (err) {
      console.error(err);
      setError("❌ Error al crear el producto/servicio.");
    }
  };

  return (
    <div className="content-area">
      <div className="card">
        <h2>Crear Producto o Servicio</h2>

        <form onSubmit={handleSubmit}>
          <label>Nombre:</label>
          <input
            type="text"
            className="form-input"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />

          <label>Precio (€):</label>
          <input
            type="number"
            className="form-input"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
          />

          <label>Tipo de IVA:</label>
          <select
            className="form-select"
            value={selectedIvaId}
            onChange={(e) => setSelectedIvaId(e.target.value)}
          >
            <option value="">-- Selecciona un IVA --</option>
            {ivaTypes.map((iva) => (
              <option key={iva.id} value={iva.id}>
                {iva.name} ({iva.percentage}%)
              </option>
            ))}
          </select>

          <label>Tipo:</label>
          <select
            className="form-select"
            value={type}
            onChange={(e) => setType(e.target.value)}
          >
            <option value="PRODUCT">Producto</option>
            <option value="SERVICE">Servicio</option>
          </select>

          <label>Estado:</label>
          <select
            className="form-select"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
          >
            <option value="ACTIVE">Activo</option>
            <option value="INACTIVE">Inactivo</option>
          </select>

          <ErrorMessage message={error} type="error" />
          <ErrorMessage message={success} type="success" />

          <button className="btn btn-primary" type="submit" style={{ marginTop: "1rem" }}>
            💾 Guardar
          </button>
        </form>
      </div>
    </div>
  );
};

export default ProductForm;
