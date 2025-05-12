import React, { useEffect, useState } from 'react';
import api from '../api/axiosConfig';
import '../components/styles/ContentArea.css';

const ProfilePage = () => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const res = await api.get('/users/me');
      setProfile(res.data);
      setLoading(false);
    } catch (err) {
      console.error('Error al cargar perfil', err);
      setError('❌ Error al cargar perfil');
      setLoading(false);
    }
  };

  if (loading) return <div className="card"><p>Cargando perfil...</p></div>;
  if (error) return <div className="card"><p style={{ color: 'red' }}>{error}</p></div>;

  return (
    <div className="card">
      <h2>Mi Perfil</h2>

      {/* Datos Personales */}
      <div className="profile-section">
        <h3>Datos Personales</h3>
        <p><strong>Nombre:</strong> {profile.name} {profile.surname}</p>
        <p><strong>Email:</strong> {profile.email}</p>
        <p><strong>Teléfono:</strong> {profile.phoneNumber}</p>
        <p><strong>Dirección:</strong> {profile.address}</p>
        <p><strong>Fecha de Registro:</strong> {profile.registerDate}</p>
      </div>

      {/* Rol y Estado */}
      <div className="profile-section">
        <h3>Rol y Estado</h3>
        <p><strong>Rol:</strong> {profile.roleName}</p>
        <p><strong>Estado:</strong> {profile.status}</p>
      </div>

      {/* Notificaciones */}
      <div className="profile-section">
        <h3>Notificaciones</h3>
        {profile.notifications && profile.notifications.length > 0 ? (
          <div className="table-wrapper">
            <table className="styled-table">
              <thead>
                <tr>
                  <th>Título</th>
                  <th>Mensaje</th>
                  <th>Fecha</th>
                  <th>Tipo</th>
                </tr>
              </thead>
              <tbody>
                {profile.notifications.map((n) => (
                  <tr key={n.id}>
                    <td>{n.title}</td>
                    <td>{n.message}</td>
                    <td>{n.shippingDate.split('T')[0]}</td>
                    <td>{n.type}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p>No tienes notificaciones.</p>
        )}
      </div>

      {/* Historial si es Student */}
      {profile.student && (
        <div className="profile-section">
          <h3>Historial de Estudiante</h3>
          {profile.student.histories && profile.student.histories.length > 0 ? (
            <div className="table-wrapper">
              <table className="styled-table">
                <thead>
                  <tr>
                    <th>Fecha</th>
                    <th>Tipo de Evento</th>
                    <th>Descripción</th>
                  </tr>
                </thead>
                <tbody>
                  {profile.student.histories.map((h) => (
                    <tr key={h.id}>
                      <td>{h.eventDate}</td>
                      <td>{h.eventType}</td>
                      <td>{h.description}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          ) : (
            <p>No tienes eventos registrados.</p>
          )}
        </div>
      )}
    </div>
  );
};

export default ProfilePage;
