import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import "../styles/ContentArea.css";

const TrainingSessionList = () => {
  const [sessions, setSessions] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      const res = await api.get("/training-sessions/getAll");
      setSessions(res.data);
    } catch (err) {
      console.error(err);
      setError("❌ Error al cargar las sesiones.");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Seguro que quieres eliminar esta sesión?")) return;

    try {
      await api.delete(`/training-sessions/delete/${id}`);
      setSessions(sessions.filter((s) => s.id !== id));
    } catch (err) {
      console.error(err);
      alert("❌ Error al eliminar la sesión.");
    }
  };

  return (
    <div className="card">
      <h2>Sesiones de Entrenamiento</h2>
      {error ? (
        <p style={{ color: "red" }}>{error}</p>
      ) : (
        <div className="table-wrapper">
          <table className="styled-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Grupo</th>
                <th>Fecha</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              {sessions.map((s) => (
                <tr key={s.id}>
                  <td>{s.id}</td>
                  <td>{s.trainingGroupId}</td>
                  <td>{new Date(s.date).toLocaleString()}</td>
                  <td>{s.status}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default TrainingSessionList;
