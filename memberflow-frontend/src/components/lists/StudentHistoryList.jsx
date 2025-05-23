import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const StudentHistoryList = () => {
  const [histories, setHistories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingId, setEditingId] = useState(null);
  const [editedData, setEditedData] = useState({});
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchHistories();
  }, []);

  const fetchHistories = async () => {
    try {
      const res = await api.get("/student-history/getAll");
      setHistories(res.data);
    } catch (err) {
      console.error("Error al cargar historial de estudiantes", err);
      setErrorMsg("❌ Error al cargar historial.");
    } finally {
      setLoading(false);
    }
  };

  const handleEditClick = (h) => {
    setEditingId(h.id);
    setEditedData({
      eventDate: h.eventDate,
      eventType: h.eventType || "",
      description: h.description || "",
      studentId: h.studentId,
    });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedData((prev) => ({ ...prev, [name]: value }));
  };

  const handleUpdate = async () => {
    try {
      await api.put(`/student-history/update/${editingId}`, editedData);
      setSuccessMsg("✅ Evento actualizado correctamente.");
      setErrorMsg("");
      setEditingId(null);
      fetchHistories();
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al actualizar el evento.");
      setSuccessMsg("");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Seguro que quieres eliminar este evento del historial?")) return;

    try {
      await api.delete(`/student-history/delete/${id}`);
      setHistories((prev) => prev.filter((h) => h.id !== id));
      setSuccessMsg("✅ Evento eliminado correctamente.");
      setErrorMsg("");
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al eliminar el evento.");
      setSuccessMsg("");
    }
  };

  if (loading)
    return (
      <div className="card">
        <p>Cargando historial...</p>
      </div>
    );

  return (
    <div className="card">
      <h2>Historial de Estudiantes</h2>

      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />

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
                  {editingId === h.id ? (
                    <>
                      <td>
                        {h.student?.user?.name} {h.student?.user?.surname}
                      </td>
                      <td>
                        <input
                          type="date"
                          name="eventDate"
                          value={editedData.eventDate}
                          onChange={handleChange}
                        />
                      </td>
                      <td>
                        <input
                          name="eventType"
                          value={editedData.eventType}
                          onChange={handleChange}
                        />
                      </td>
                      <td>
                        <input
                          name="description"
                          value={editedData.description}
                          onChange={handleChange}
                        />
                      </td>
                      <td>
                        <button className="edit-btn" onClick={handleUpdate}>✅</button>
                        <button className="delete-btn" onClick={() => setEditingId(null)}>❌</button>
                      </td>
                    </>
                  ) : (
                    <>
                      <td>
                        {h.student?.user?.name} {h.student?.user?.surname}
                        {(!h.student || !h.student.user) && `(ID: ${h.studentId})`}
                      </td>
                      <td>{h.eventDate}</td>
                      <td>{h.eventType}</td>
                      <td>{h.description}</td>
                      <td>
                        <button className="edit-btn" onClick={() => handleEditClick(h)}>✏️</button>
                        <button className="delete-btn" onClick={() => handleDelete(h.id)}>🗑️</button>
                      </td>
                    </>
                  )}
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
