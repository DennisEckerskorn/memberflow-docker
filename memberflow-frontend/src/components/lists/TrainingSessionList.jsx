import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import "../styles/ContentArea.css";
import ErrorMessage from "../common/ErrorMessage";

const TrainingSessionList = () => {
  const [sessions, setSessions] = useState([]);
  const [groups, setGroups] = useState([]);
  const [error, setError] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [sessionRes, groupRes] = await Promise.all([
        api.get("/training-sessions/getAll"),
        api.get("/training-groups/getAll")
      ]);
      setSessions(sessionRes.data);
      setGroups(groupRes.data);
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
      setSuccessMsg("✅ Sesión eliminada correctamente.");
    } catch (err) {
      console.error(err);
      setError("❌ Error al eliminar la sesión.");
    }
  };

  const getGroupName = (id) => {
    const group = groups.find((g) => g.id === id);
    return group ? `${group.name} (${group.level})` : `Grupo ID ${id}`;
  };

  const renderStatus = (status) => {
    const baseClass = "status-label";
    switch (status) {
      case "ACTIVE":
        return <span className={`${baseClass} status-active`}>🟢 {status}</span>;
      case "CANCELLED":
        return <span className={`${baseClass} status-cancelled`}>🔴 {status}</span>;
      case "FINISHED":
        return <span className={`${baseClass} status-finished`}>⚪ {status}</span>;
      default:
        return <span className={baseClass}>{status}</span>;
    }
  };

  return (
    <div className="card">
      <h2>Sesiones de Entrenamiento</h2>

      <ErrorMessage message={error} type="error" />
      <ErrorMessage message={successMsg} type="success" />

      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Grupo</th>
              <th>Fecha y Hora</th>
              <th>Estado</th>
            </tr>
          </thead>
          <tbody>
            {sessions.length === 0 ? (
              <tr>
                <td colSpan="5">No hay sesiones registradas.</td>
              </tr>
            ) : (
              sessions.map((s) => (
                <tr key={s.id}>
                  <td>{s.id}</td>
                  <td>{getGroupName(s.trainingGroupId)}</td>
                  <td>{new Date(s.date).toLocaleString()}</td>
                  <td>{renderStatus(s.status)}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TrainingSessionList;
