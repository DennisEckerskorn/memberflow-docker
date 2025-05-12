import React from 'react';
import { useNavigate } from 'react-router-dom';
import { getRoleFromToken } from '../../utils/jwtHelper';
import '../styles/Topbar.css';

const Topbar = () => {
  const navigate = useNavigate();
  const role = getRoleFromToken();

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const roleLabel = {
    FULL_ACCESS: 'Admin',
    MANAGE_STUDENTS: 'Profesor',
    VIEW_OWN_DATA: 'Estudiante'
  }[role] || 'Usuario';

  return (
    <header className="topbar">
      <div className="topbar-left">
        <h1>MemberFlow</h1>
        <span className="topbar-role">| {roleLabel}</span>
      </div>
      <div className="topbar-right">
        <button className="logout-button" onClick={handleLogout}>Cerrar sesión</button>
      </div>
    </header>
  );
};

export default Topbar;
