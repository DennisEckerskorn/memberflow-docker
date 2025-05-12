import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getRoleFromToken } from '../../utils/jwtHelper';
import '../styles/Sidebar.css';

const Sidebar = () => {
  const navigate = useNavigate();
  const role = getRoleFromToken();
  
  return (
    <div className="sidebar">
      <h4>Menú</h4>
      <div className='sidebar-scroll'>
      {/* Botón común para todos: Mi Perfil */}
      <button onClick={() => navigate('/profile')}>👤 Mi Perfil</button>

      {role === 'FULL_ACCESS' && (
        <>
          <button onClick={() => navigate('/admin/dashboard')}>🏠 Dashboard Admin</button>

          
            <div className="submenu">
              <h4>👥 Administración de Usuarios</h4>
              <button onClick={() => navigate('/admin/user-management/users/create')}>➕ Crear Usuario</button>
              <button onClick={() => navigate('/admin/user-management/users/list')}>📋 Ver Usuarios</button>
              <button onClick={() => navigate('/admin/user-management/notifications/create')}>🔔 Crear Notificación</button>
              <button onClick={() => navigate('/admin/user-management/notifications/list')}>📨 Ver Notificaciones</button>
              <button onClick={() => navigate('/admin/user-management/student-history/create')}>🕒 Crear Historial</button>
              <button onClick={() => navigate('/admin/user-management/student-history/list')}>📜 Ver Historial</button>
            </div>
        
            <div className="submenu">
              <h4>📚 Administración de Clases</h4>
              <button onClick={() => navigate('/admin/class-management/training-groups/create')}>➕ Crear Grupo</button>
              <button onClick={() => navigate('/admin/class-management/training-groups/list')}>👥 Ver Grupos</button>
              <button onClick={() => navigate('/admin/class-management/training-groups/manage-students')}>🧑‍🏫 Administrar Grupos</button>
              <button onClick={() => navigate('/admin/class-management/training-groups/view-timetable')}>🗓️ Ver Horario</button>
              <button onClick={() => navigate('/admin/class-management/training-session/list')}>📆 Ver Sesiones</button>
              <button onClick={() => navigate('/admin/class-management/assistance/create')}>📝 Registrar Asistencia</button>
              <button onClick={() => navigate('/admin/class-management/assistance/list')}>📋 Ver Asistencias</button>
              <button onClick={() => navigate('/admin/class-management/memberships/create')}>➕ Crear Membresía</button>
              <button onClick={() => navigate('/admin/class-management/memberships/list')}>🏷️ Ver Membresías</button>
            </div>

            <div className='submenu'>
              <h4>💵 Administración de Pagos y Facturas</h4>
              <button onClick={() => navigate('')}>🧾 Crear Factura</button>
              <button onClick={() => navigate('')}>📄 Ver Facturas</button>
              <button onClick={() => navigate('')}>💳 Nuevo Pago</button>
              <button onClick={() => navigate('')}>🛒 Añadir Productos</button>
              <button onClick={() => navigate('')}>📦 Ver Productos</button>
              <button onClick={() => navigate('')}>💱 Añadir Tipo de IVA</button>
            </div>
          
        </>
      )}

      {role === 'MANAGE_STUDENTS' && (
        <>
          <button onClick={() => navigate('/teacher/dashboard')}>🏠 Dashboard Profesor</button>
          <button onClick={() => navigate('/teacher/students')}>👨‍🎓 Mis Estudiantes</button>
          <button onClick={() => navigate('/teacher/classes')}>📚 Mis Clases</button>
        </>
      )}

      {role === 'VIEW_OWN_DATA' && (
        <>
          <button onClick={() => navigate('/student/dashboard')}>🏠 Mi Panel</button>
          <button onClick={() => navigate('/student/history')}>🕒 Mi Historial</button>
        </>
      )}
      </div>
    </div>
  );
};

export default Sidebar;
