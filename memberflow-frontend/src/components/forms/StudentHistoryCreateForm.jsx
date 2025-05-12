import React, { useState, useEffect } from 'react';
import api from '../../api/axiosConfig';

const StudentHistoryCreateForm = () => {
  const today = new Date().toISOString().split('T')[0];

  const [historyData, setHistoryData] = useState({
    studentId: '',
    eventDate: today,
    eventType: '',
    description: '',
  });

  const [students, setStudents] = useState([]);
  const [successMsg, setSuccessMsg] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    try {
      const res = await api.get('/students/getAll');
      setStudents(res.data);
    } catch (err) {
      console.error('Error al cargar estudiantes', err);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setHistoryData({ ...historyData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMsg('');
  
    try {
      await api.post('/student-history/create', {
        ...historyData,
      });
      setSuccessMsg('✅ Evento creado correctamente');
      resetForm();
    } catch (err) {
      console.error(err);
      setError('❌ Error al crear el evento');
    }
  };

  const resetForm = () => {
    setHistoryData({
      studentId: '',
      eventDate: today,
      eventType: '',
      description: '',
    });
  };

  return (
    <div className="card">
      <h2>Crear Evento de Historial</h2>

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
        <select name="studentId" value={historyData.studentId} onChange={handleChange} required>
          <option value="">Selecciona un Estudiante</option>
          {students.map(student => (
            <option key={student.id} value={student.id}>
              {student.user?.name} {student.user?.surname} (ID: {student.id})
            </option>
          ))}
        </select>

        <input type="date" name="eventDate" value={historyData.eventDate} onChange={handleChange} required />

        <input type="text" name="eventType" placeholder="Tipo de evento" value={historyData.eventType} onChange={handleChange} required />

        <textarea name="description" placeholder="Descripción" value={historyData.description} onChange={handleChange} rows="4" />

        <button type="submit">Crear Evento</button>
      </form>

      {successMsg && <p style={{ color: 'green' }}>{successMsg}</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
};

export default StudentHistoryCreateForm;
