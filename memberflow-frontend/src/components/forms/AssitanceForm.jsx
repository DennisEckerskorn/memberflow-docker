import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const AssistanceForm = () => {
  const [students, setStudents] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [filteredSessions, setFilteredSessions] = useState([]);
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
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));

    if (name === "studentId") {
      const student = students.find((s) => s.id === parseInt(value));
      if (student) {
        const groupIds = student.trainingGroups?.map((g) => g.id) || [];
        const filtered = sessions.filter((s) =>
          groupIds.includes(s.trainingGroupId)
        );
        setFilteredSessions(filtered);
      } else {
        setFilteredSessions([]);
      }
    }
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
      setFilteredSessions([]);
    } catch (err) {
      console.error(err);
      const msg = err.response?.data?.message || "❌ Error al registrar asistencia.";
      setErrorMsg(msg);
    }
  };

  return (
    <div className="card">
      <h2>Registrar Asistencia</h2>
      <form onSubmit={handleSubmit} className="form-column">
        <label>Estudiante:</label>
        <select
          name="studentId"
          value={formData.studentId}
          onChange={handleChange}
          required
        >
          <option value="">-- Selecciona estudiante --</option>
          {students.map((s) => (
            <option key={s.id} value={s.id}>
              {s.user?.name} {s.user?.surname}
            </option>
          ))}
        </select>

        <label>Sesión:</label>
        {formData.studentId && filteredSessions.length === 0 ? (
          <p style={{ color: "#e74c3c", margin: "5px 0 15px" }}>
            ⚠️ Este estudiante no tiene sesiones disponibles según sus grupos asignados.
          </p>
        ) : (
          <select
            name="sessionId"
            value={formData.sessionId}
            onChange={handleChange}
            required
            disabled={filteredSessions.length === 0}
          >
            <option value="">-- Selecciona sesión --</option>
            {filteredSessions.map((s) => (
              <option key={s.id} value={s.id}>
                {`${new Date(s.date).toLocaleString()} - Grupo: ${s.trainingGroup?.name || s.trainingGroupId}`}
              </option>
            ))}
          </select>
        )}

        <button type="submit" disabled={!formData.studentId || !formData.sessionId}>
          Registrar
        </button>

        <ErrorMessage message={successMsg} type="success" />
        <ErrorMessage message={errorMsg} type="error" />
      </form>
    </div>
  );
};

export default AssistanceForm;
