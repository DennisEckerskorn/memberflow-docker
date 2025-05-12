import React, { useState } from 'react';
import api from '../../api/axiosConfig';

const TeacherForm = ({ userId, onFinish }) => {
  const [teacherData, setTeacherData] = useState({
    userId: userId,
    discipline: ''
  });

  const handleChange = (e) => {
    setTeacherData({ ...teacherData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/teachers/create', teacherData);
      alert('✅ Profesor creado correctamente');
      onFinish();
    } catch (err) {
      console.error(err);
      alert('❌ Error al crear profesor');
    }
  };

  return (
    <div className="card" style={{ marginTop: '20px' }}>
      <h3>Datos de Profesor</h3>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
        <input type="text" name="discipline" placeholder="Disciplina" value={teacherData.discipline} onChange={handleChange} required />
        <button type="submit">Guardar Profesor</button>
      </form>
    </div>
  );
};

export default TeacherForm;
