import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import "../styles/ContentArea.css";

const AssistanceList = () => {
  const [assistances, setAssistances] = useState([]);
  const [students, setStudents] = useState([]);
  const [sessions, setSessions] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [aRes, sRes, tsRes] = await Promise.all([
        api.get("/assistances/getAll"),
        api.get("/students/getAll"),
        api.get("/training-sessions/getAll"),
      ]);
      setAssistances(aRes.data);
      setStudents(sRes.data);
      setSessions(tsRes.data);
    } catch (err) {
      console.error(err);
      setError("Error al cargar asistencias.");
    }
  };

  const getStudentName = (id) => {
    const s = students.find((s) => s.id === id);
    return s ? `${s.user?.name} ${s.user?.surname}` : `(ID: ${id})`;
  };

  const getSessionInfo = (id) => {
    const s = sessions.find((s) => s.id === id);
    if (!s) return "Grupo no encontrado";
    return s.trainingGroupId || "Grupo desconocido";
  };

  return (
    <div className="card">
      <h2>Lista de Asistencias</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>Estudiante</th>
              <th>Grupo</th>
              <th>Fecha</th>
            </tr>
          </thead>
          <tbody>
            {assistances.map((a) => (
              <tr key={a.id}>
                <td>{getStudentName(a.studentId)}</td>
                <td>{getSessionInfo(a.sessionId)}</td>
                <td>{new Date(a.date).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AssistanceList;
