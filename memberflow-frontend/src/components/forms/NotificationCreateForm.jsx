import React, { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';
import '../styles/ContentArea.css';

const NotificationCreateForm = () => {
  const today = new Date().toISOString().split('T')[0];

  const [notificationData, setNotificationData] = useState({
    title: '',
    message: '',
    shippingDate: today,
    type: '',
    status: 'ACTIVE',
    userIds: [],
  });

  const [users, setUsers] = useState([]);
  const [successMsg, setSuccessMsg] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const res = await api.get('/users/getAll');
      setUsers(res.data);
    } catch (err) {
      console.error('Error al cargar usuarios', err);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNotificationData({ ...notificationData, [name]: value });
  };

  const handleCheckboxChange = (e, userId) => {
    const selectedUserIds = [...notificationData.userIds];
    if (e.target.checked) {
      selectedUserIds.push(userId);
    } else {
      const index = selectedUserIds.indexOf(userId);
      if (index !== -1) {
        selectedUserIds.splice(index, 1);
      }
    }
    setNotificationData({ ...notificationData, userIds: selectedUserIds });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMsg('');

    try {
      await api.post('/notifications/create', {
        ...notificationData,
        shippingDate: new Date(notificationData.shippingDate).toISOString(),
      });
      setSuccessMsg('✅ Notificación creada correctamente');
      resetForm();
    } catch (err) {
      console.error(err);
      setError('❌ Error al crear la notificación');
    }
  };

  const resetForm = () => {
    setNotificationData({
      title: '',
      message: '',
      shippingDate: today,
      type: '',
      status: 'ACTIVE',
      userIds: [],
    });
  };

  return (
    <div className="card">
      <h2>Crear nueva notificación</h2>

      <form onSubmit={handleSubmit} className="form-column">
        <input type="text" name="title" placeholder="Título" value={notificationData.title} onChange={handleChange} required />
        <textarea name="message" placeholder="Mensaje" value={notificationData.message} onChange={handleChange} rows="4" required />

        <input type="date" name="shippingDate" value={notificationData.shippingDate} onChange={handleChange} required />

        <input type="text" name="type" placeholder="Tipo de notificación (opcional)" value={notificationData.type} onChange={handleChange} />

        <select name="status" value={notificationData.status} onChange={handleChange} required>
          <option value="ACTIVE">Activo</option>
          <option value="INACTIVE">Inactivo</option>
        </select>

        <label>Asignar a usuarios:</label>
        <div className="user-select-box">
          {users.map(user => (
            <label key={user.id} className="user-checkbox">
              <input
                type="checkbox"
                value={user.id}
                checked={notificationData.userIds.includes(user.id)}
                onChange={(e) => handleCheckboxChange(e, user.id)}
              />
              {user.name} {user.surname} ({user.email})
            </label>
          ))}
        </div>

        <button type="submit">Crear notificación</button>
      </form>

      {successMsg && <p style={{ color: 'green' }}>{successMsg}</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
};

export default NotificationCreateForm;
