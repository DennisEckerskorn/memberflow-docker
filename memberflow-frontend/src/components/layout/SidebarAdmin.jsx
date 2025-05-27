import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Sidebar.css';

const SidebarAdmin = () => {
  const navigate = useNavigate();

  return (
    <div className="sidebar">
      <h4>Menú</h4>
      <div className="sidebar-scroll">
        <button onClick={() => navigate('/profile')}>👤 Mi Perfil</button>
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
          <button onClick={() => navigate('/admin/class-management/memberships/details')}>➕ Detalles de las Membresías</button>
          <button onClick={() => navigate('/admin/class-management/memberships/list')}>🏷️ Asignar Membresías</button>
        </div>

        <div className="submenu">
          <h4>💵 Finanzas</h4>
          <button onClick={() => navigate('/admin/finance/invoices/create')}>🧾 Crear Factura</button>
          <button onClick={() => navigate('/admin/finance/invoices/list')}>📄 Ver Facturas</button>
          <button onClick={() => navigate('/admin/finance/payments/create')}>📄 Nuevo Pago</button>
          <button onClick={() => navigate('/admin/finance/products/create')}>🛒 Añadir Productos</button>
          <button onClick={() => navigate('/admin/finance/products/list')}>📦 Ver Productos</button>
          <button onClick={() => navigate('/admin/finance/ivatypes/create')}>💱 Añadir Tipo de IVA</button>
        </div>
      </div>
    </div>
  );
};

export default SidebarAdmin;
