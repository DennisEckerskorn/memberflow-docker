import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import "../styles/ContentArea.css";

const StudentHistoryList = () => {
  const [histories, setHistories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchHistories();
  }, []);

  const fetchHistories = async () => {
    try {
      const res = await api.get("/student-history/getAll");
      setHistories(res.data);
      setLoading(false);
    } catch (err) {
      console.error("Error al cargar historial de estudiantes", err);
      setError("Error al cargar historial");
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (
      !window.confirm("¿Seguro que quieres eliminar este evento del historial?")
    )
      return;

    try {
      await api.delete(`/student-history/delete/${id}`);
      setHistories(histories.filter((h) => h.id !== id));
      alert("Evento eliminado correctamente");
    } catch (err) {
      console.error(err);
      alert("Error al eliminar el evento");
    }
  };

  if (loading)
    return (
      <div className="card">
        <p>Cargando historial...</p>
      </div>
    );
  if (error)
    return (
      <div className="card">
        <p style={{ color: "red" }}>{error}</p>
      </div>
    );

  return (
    <div className="card">
      <h2>Historial de Estudiantes</h2>

      {histories.length === 0 ? (
        <p>No hay eventos registrados.</p>
      ) : (
        <div className="table-wrapper">
          <table className="styled-table">
            <thead>
              <tr>
                <th>Estudiante</th>
                <th>Fecha</th>
                <th>Tipo</th>
                <th>Descripción</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {histories.map((h) => (
                <tr key={h.id}>
                  <td>
                    {h.student?.user?.name} {h.student?.user?.surname}
                    {(!h.student || !h.student.user) && `(ID: ${h.studentId})`}
                  </td>
                  <td>{h.eventDate}</td>
                  <td>{h.eventType}</td>
                  <td>{h.description}</td>
                  <td>
                    <button
                      className="edit-button"
                      onClick={() => alert("Editar aún no implementado")}
                    >
                      ✏️
                    </button>
                    <button
                      className="delete-button"
                      onClick={() => handleDelete(h.id)}
                    >
                      🗑️
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default StudentHistoryList;
