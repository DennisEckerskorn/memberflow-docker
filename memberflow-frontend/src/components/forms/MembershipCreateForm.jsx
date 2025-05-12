import React, { use, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";


const MembershipForm = () => {
  const [formData, setFormData] = useState({
    startDate: "",
    endDate: "",
    type: "BASIC",
    status: "ACTIVE",
  });

  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMsg("");
    setErrorMsg("");

    try {
        console.log("Payload:", formData);

      await api.post("/memberships/create", formData);
      setSuccessMsg("✅ Membresía creada correctamente");
      setFormData({
        startDate: "",
        endDate: "",
        type: "BASIC",
        status: "ACTIVE",
      });
    } catch (err) {
      console.error(err);
      const msg =
        err.response?.data?.message || "❌ Error al crear la membresía";
        setErrorMsg(msg);
    }
  };

  return (
    <div className="card">
      <h2>Crear Membresía nueva</h2>
      <form onSubmit={handleSubmit}>
        <label>Fecha de inicio de la membresía:</label>
        <input
          type="date"
          name="startDate"
          value={formData.startDate}
          onChange={handleChange}
          required
        />
        <label>Fecha fin de la membresía:</label>
        <input
          type="date"
          name="endDate"
          value={formData.endDate}
          onChange={handleChange}
          required
        />

        <label>Selecciona el tipo de membresía:</label>
        <select
          name="type"
          value={formData.type}
          onChange={handleChange}
          required
        >
          <option value="BASIC">Básico</option>
          <option value="ADVANCED">Avanzado</option>
          <option value="PREMIUM">Premium</option>
          <option value="NO_LIMIT">Ilimitado</option>
          <option value="TRIAL">Prueba</option>
        </select>

        <label>Estado de la membresía:</label>
        <select
          name="status"
          value={formData.status}
          onChange={handleChange}
          required
        >
          <option value="ACTIVE">Activo</option>
          <option value="INACTIVE">Inactivo</option>
          <option value="SUSPENDED">Suspendido / Expirado</option>
        </select>

        <button type="submit">Crear nueva membresía</button>
      </form>
      <ErrorMessage type="success" message={successMsg} />
      <ErrorMessage type="error" message={errorMsg} />
    </div>
  );
};

export default MembershipForm;
