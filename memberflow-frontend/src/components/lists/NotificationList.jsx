// src/components/lists/NotificationList.jsx
import React, { useEffect, useState } from 'react';
import api from '../../api/axiosConfig';
import '../styles/ContentArea.css'; // Tu estilo general de tarjetas y tablas

const NotificationList = () => {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchNotifications();
  }, []);

  const fetchNotifications = async () => {
    try {
      const res = await api.get('/notifications/getAll');
      setNotifications(res.data);
      setLoading(false);
    } catch (err) {
      console.error('Error al cargar notificaciones', err);
      setLoading(false);
    }
  };

  if (loading) return <div className="card"><p>Cargando notificaciones...</p></div>;

  return (
    <div className="card">
      <h2>Listado de Notificaciones</h2>

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
              </tr>
            </thead>
            <tbody>
              {notifications.map((n) => (
                <tr key={n.id}>
                  <td>{n.title}</td>
                  <td>{n.message}</td>
                  <td>{n.shippingDate.split('T')[0]}</td>
                  <td>{n.type}</td>
                  <td>{n.status}</td>
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
