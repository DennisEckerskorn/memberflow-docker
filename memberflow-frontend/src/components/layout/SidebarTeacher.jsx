import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Sidebar.css';

const SidebarTeacher = () => {
  const navigate = useNavigate();

  return (
    <div className="sidebar">
      <h4>Menú</h4>
      <div className="sidebar-scroll">
        <button onClick={() => navigate('/profile')}>👤 Mi Perfil</button>
        <button onClick={() => navigate('/teacher/dashboard')}>🏠 Dashboard Profesor</button>

        <div className="submenu">
          <h4>👨‍🏫 Gestión del Profesor</h4>
          <button onClick={() => navigate('/teacher/students')}>👨‍🎓 Mis Estudiantes</button>
          <button onClick={() => navigate('/teacher/classes')}>📚 Mis Clases</button>
        </div>
      </div>
    </div>
  );
};

export default SidebarTeacher;
