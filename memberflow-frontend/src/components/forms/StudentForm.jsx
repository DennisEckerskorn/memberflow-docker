import React, { useState } from 'react';
import api from '../../api/axiosConfig';

const StudentForm = ({ onFinish }) => {
  const today = new Date().toISOString().split('T')[0];

  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    email: '',
    password: '',
    phoneNumber: '',
    address: '',
    status: 'ACTIVE',
    dni: '',
    birthdate: today,
    belt: '',
    parentName: '',
    medicalReport: ''
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/students/register', {
        name: formData.name,
        surname: formData.surname,
        email: formData.email,
        password: formData.password,
        phoneNumber: formData.phoneNumber,
        address: formData.address,
        status: formData.status,
        dni: formData.dni,
        birthdate: formData.birthdate,
        belt: formData.belt,
        parentName: formData.parentName,
        medicalReport: formData.medicalReport
      });
      alert('✅ Estudiante creado correctamente');
      onFinish();
    } catch (err) {
      console.error(err);
      alert('❌ Error al crear estudiante');
    }
  };

  return (
    <div className="card" style={{ marginTop: '20px' }}>
      <h2>Crear Estudiante</h2>
      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
        <h3>Datos de Usuario</h3>
        <input type="text" name="name" placeholder="Nombre" value={formData.name} onChange={handleChange} required />
        <input type="text" name="surname" placeholder="Apellido" value={formData.surname} onChange={handleChange} required />
        <input type="email" name="email" placeholder="Correo electrónico" value={formData.email} onChange={handleChange} required />
        <input type="password" name="password" placeholder="Contraseña" value={formData.password} onChange={handleChange} required />
        <input type="text" name="phoneNumber" placeholder="Teléfono" value={formData.phoneNumber} onChange={handleChange} required />
        <input type="text" name="address" placeholder="Dirección" value={formData.address} onChange={handleChange} required />

        <h3>Datos de Estudiante</h3>
        <input type="text" name="dni" placeholder="DNI" value={formData.dni} onChange={handleChange} required />
        <input type="date" name="birthdate" value={formData.birthdate} onChange={handleChange} required />
        <select name="belt" value={formData.belt} onChange={handleChange} required>
          <option value="">Selecciona un cinturón</option>
          <option value="Blanco">Blanco</option>
          <option value="Azul">Azul</option>
          <option value="Morado">Morado</option>
          <option value="Marrón">Marrón</option>
          <option value="Negro">Negro</option>
        </select>
        <input type="text" name="parentName" placeholder="Nombre del Tutor (opcional)" value={formData.parentName} onChange={handleChange} />
        <input type="text" name="medicalReport" placeholder="Informe Médico (opcional)" value={formData.medicalReport} onChange={handleChange} />

        <button type="submit">Guardar Estudiante</button>
      </form>
    </div>
  );
};

export default StudentForm;
