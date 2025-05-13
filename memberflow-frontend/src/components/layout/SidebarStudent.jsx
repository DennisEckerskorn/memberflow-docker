import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Sidebar.css';

const SidebarStudent = () => {
  const navigate = useNavigate();

  return (
    <div className="sidebar">
      <h4>Menú</h4>
      <div className="sidebar-scroll">
        <button onClick={() => navigate('/profile')}>👤 Mi Perfil</button>
        <button onClick={() => navigate('/student/dashboard')}>🏠 Mi Panel</button>

        <div className="submenu">
          <h4>🎓 Opciones de Estudiante</h4>
          <button onClick={() => navigate('/student/history')}>🕒 Mi Historial</button>
          <button onClick={() => navigate('/student/timetable')}>📅 Ver Horario</button>
          <button onClick={() => navigate('/student/assistance')}>📋 Ver Asistencia</button>
        </div>
      </div>
    </div>
  );
};

export default SidebarStudent;
