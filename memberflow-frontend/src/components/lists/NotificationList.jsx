import React, { useEffect, useState } from "react";
import api from "../../api/axiosConfig";
import ErrorMessage from "../common/ErrorMessage";
import "../styles/ContentArea.css";

const NotificationList = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingId, setEditingId] = useState(null);
  const [editedData, setEditedData] = useState({});
  const [errorMsg, setErrorMsg] = useState("");
  const [successMsg, setSuccessMsg] = useState("");

  useEffect(() => {
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    try {
      const res = await api.get("/notifications/getAll");
      setNotifications(res.data);
    } catch (err) {
      console.error("Error al cargar notificaciones", err);
      setErrorMsg("❌ Error al cargar notificaciones.");
    } finally {
      setLoading(false);
    }
  };

  const handleEditClick = (n) => {
    setEditingId(n.id);
    setEditedData({
      title: n.title,
      message: n.message,
      shippingDate: n.shippingDate.split("T")[0],
      type: n.type || "",
      status: n.status,
    });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedData((prev) => ({ ...prev, [name]: value }));
  };

  const handleUpdate = async () => {
    try {
      await api.put(`/notifications/update/${editingId}`, {
        ...editedData,
        shippingDate: new Date(editedData.shippingDate).toISOString(),
      });
      setSuccessMsg("✅ Notificación actualizada correctamente");
      setErrorMsg("");
      setEditingId(null);
      fetchNotifications();
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al actualizar la notificación");
      setSuccessMsg("");
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Eliminar esta notificación?")) return;
    try {
      await api.delete(`/notifications/delete/${id}`);
      setNotifications((prev) => prev.filter((n) => n.id !== id));
      setSuccessMsg("✅ Notificación eliminada");
      setErrorMsg("");
    } catch (err) {
      console.error(err);
      setErrorMsg("❌ Error al eliminar la notificación");
    }
  };

  if (loading)
    return (
      <div className="card">
        <p>Cargando notificaciones...</p>
      </div>
    );

  return (
    <div className="card">
      <h2>Listado de Notificaciones</h2>

      {/* ✅ Mensajes reutilizables */}
      <ErrorMessage message={errorMsg} type="error" />
      <ErrorMessage message={successMsg} type="success" />

      {notifications.length === 0 ? (
        <p>No hay notificaciones registradas.</p>
      ) : (
        <div className="table-wrapper">
          <table className="styled-table">
            <thead>
              <tr>
                <th>Título</th>
                <th>Mensaje</th>
                <th>Fecha de Envío</th>
                <th>Tipo</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {notifications.map((n) => (
                <tr key={n.id}>
                  {editingId === n.id ? (
                    <>
                      <td>
                        <input
                          name="title"
                          value={editedData.title}
                          onChange={handleChange}
                        />
                      </td>
                      <td>
                        <textarea
                          name="message"
                          rows="2"
                          value={editedData.message}
                          onChange={handleChange}
                        />
                      </td>
                      <td>
                        <input
                          type="date"
                          name="shippingDate"
                          value={editedData.shippingDate}
                          onChange={handleChange}
                        />
                      </td>
                      <td>
                        <input
                          name="type"
                          value={editedData.type}
                          onChange={handleChange}
                        />
                      </td>
                      <td>
                        <select
                          name="status"
                          value={editedData.status}
                          onChange={handleChange}
                        >
                          <option value="ACTIVE">Activo</option>
                          <option value="INACTIVE">Inactivo</option>
                        </select>
                      </td>
                      <td>
                        <button className="edit-btn" onClick={handleUpdate}>
                          ✅
                        </button>
                        <button
                          className="delete-btn"
                          onClick={() => setEditingId(null)}
                        >
                          ❌
                        </button>
                      </td>
                    </>
                  ) : (
                    <>
                      <td>{n.title}</td>
                      <td>{n.message}</td>
                      <td>{n.shippingDate.split("T")[0]}</td>
                      <td>{n.type}</td>
                      <td>{n.status}</td>
                      <td>
                        <button
                          className="edit-btn"
                          onClick={() => handleEditClick(n)}
                        >
                          ✏️
                        </button>
                        <button
                          className="delete-btn"
                          onClick={() => handleDelete(n.id)}
                        >
                          🗑️
                        </button>
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

export default NotificationList;
