import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const AssistanceForm = () => {
  const [students, setStudents] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [formData, setFormData] = useState({
    studentId: "",
    sessionId: ""
  });
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    api.get("/students/getAll").then((res) => setStudents(res.data));
    api.get("/training-sessions/getAll").then((res) => setSessions(res.data));
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg("");
    setSuccessMsg("");

  const payload = {
  studentId: formData.studentId,
  sessionId: formData.sessionId,
  date: new Date(new Date().setHours(new Date().getHours() + 2)).toISOString()
};


    try {
      await api.post("/assistances/create", payload);
      setSuccessMsg("✅ Asistencia registrada correctamente.");
      setFormData({ studentId: "", sessionId: "" });
    } catch (err) {
      console.error(err);
      const msg = err.response?.data?.message || "❌ Error al registrar asistencia.";
      setErrorMsg(msg);
    }
  };

  return (
    <div className="card">
      <h2>Registrar Asistencia</h2>
      <form onSubmit={handleSubmit}>
        <select
          name="studentId"
          value={formData.studentId}
          onChange={handleChange}
          required
        >
          <option value="">Selecciona estudiante</option>
          {students.map((s) => (
            <option key={s.id} value={s.id}>
              {s.user?.name} {s.user?.surname}
            </option>
          ))}
        </select>

        <select
          name="sessionId"
          value={formData.sessionId}
          onChange={handleChange}
          required
        >
          <option value="">Selecciona sesión</option>
          {sessions.map((s) => (
            <option key={s.id} value={s.id}>
              {`${new Date(s.date).toLocaleString()} - Grupo: ${s.trainingGroup?.name || s.trainingGroupId}`}
            </option>
          ))}
        </select>

        <button type="submit">Registrar</button>
        <ErrorMessage message={successMsg} type="success" />
        <ErrorMessage message={errorMsg} type="error" />
      </form>
    </div>
  );
};

export default AssistanceForm;
