import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../components/styles/DashboardCards.css'; // lo creamos ahora

const AdminDashboard = () => {
  const navigate = useNavigate();

  const options = [
    { title: '➕ Crear Usuario', route: '/admin/user-management/users/create' },
    { title: '📜 Listar Usuarios', route: '/admin/user-management/users/list' },
    { title: '🔔 Crear Notificación', route: '/admin/user-management/notifications/create' },
    { title: '🕓 Crear Historial', route: '/admin/user-management/student-history/create' },
    { title: '📜 Ver Historial', route: '/admin/user-management/student-history/list' },
    { title: '🎓 Gestión Estudiantes', route: '/admin/user-management/students' },
    { title: '👨‍🏫 Gestión Profesores', route: '/admin/user-management/teachers' },
    { title: '🛡️ Gestión Admins', route: '/admin/user-management/admins' },
    { title: '📚 Gestión de Clases', route: '/admin/class-management' },
    { title: '💰 Finanzas', route: '/admin/finance' }
  ];

  return (
    <div className="dashboard">
      <h2>Panel de Control del Administrador</h2>
      <div className="card-grid">
        {options.map((opt, index) => (
          <div key={index} className="dashboard-card" onClick={() => navigate(opt.route)}>
            {opt.title}
          </div>
        ))}
      </div>
    </div>
  );
};

export default AdminDashboard;
