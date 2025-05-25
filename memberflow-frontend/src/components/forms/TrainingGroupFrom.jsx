import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";

const TrainingGroupForm = () => {
  const [formData, setFormData] = useState({
    name: "",
    level: "",
    schedule: "",
    teacherId: "",
    recurrenceMonths: 1, // nuevo campo
  });

  const [teachers, setTeachers] = useState([]);
  const [successMsg, setSuccessMsg] = useState("");
  const [errorMsg, setErrorMsg] = useState("");

  useEffect(() => {
    api.get("/teachers/getAll")
      .then((res) => setTeachers(res.data))
      .catch((err) => console.error("Error al cargar profesores", err));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSuccessMsg("");
    setErrorMsg("");

    try {
      await api.post("/training-groups/create", {
        name: formData.name,
        level: formData.level,
        schedule: formData.schedule,
        teacherId: parseInt(formData.teacherId),
        recurrenceMonths: parseInt(formData.recurrenceMonths), // incluir
      });
      setSuccessMsg("✅ Grupo de entrenamiento creado correctamente.");
      setFormData({ name: "", level: "", schedule: "", teacherId: "", recurrenceMonths: 1 });
    } catch (err) {
      console.error("Error al crear grupo", err);
      const backendMsg =
        err.response?.data?.message ||
        err.response?.data?.error ||
        "❌ Error al crear el grupo. Verifica los datos.";
      setErrorMsg(backendMsg);
    }
  };

  return (
    <div className="card">
      <h2>Crear Grupo de Entrenamiento</h2>
      <form onSubmit={handleSubmit} className="form-column">
        <input
          type="text"
          name="name"
          placeholder="Nombre del grupo"
          value={formData.name}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="level"
          placeholder="Nivel"
          value={formData.level}
          onChange={handleChange}
          required
        />

        <label>Horario del grupo:</label>
        <input
          type="datetime-local"
          name="schedule"
          value={formData.schedule}
          onChange={handleChange}
          required
        />

        <label>Meses de recurrencia:</label>
        <input
          type="number"
          name="recurrenceMonths"
          value={formData.recurrenceMonths}
          onChange={handleChange}
          min={1}
          max={24}
          required
        />

        <select
          name="teacherId"
          value={formData.teacherId}
          onChange={handleChange}
          required
        >
          <option value="">-- Selecciona un profesor responsable del grupo --</option>
          {teachers.map((teacher) => (
            <option key={teacher.id} value={teacher.id}>
              {teacher.user?.name} {teacher.user?.surname}
            </option>
          ))}
        </select>

        <button type="submit">Crear grupo</button>
      </form>

      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />
    </div>
  );
};

export default TrainingGroupForm;
