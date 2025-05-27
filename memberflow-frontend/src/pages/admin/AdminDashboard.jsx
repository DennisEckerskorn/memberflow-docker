import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../components/styles/DashboardCards.css';

const AdminDashboard = () => {
  const navigate = useNavigate();

  const sections = [
    {
      title: '👥 Administración de Usuarios',
      options: [
        { title: '➕ Crear Usuario', route: '/admin/user-management/users/create' },
        { title: '📋 Ver Usuarios', route: '/admin/user-management/users/list' },
        { title: '🔔 Crear Notificación', route: '/admin/user-management/notifications/create' },
        { title: '📨 Ver Notificaciones', route: '/admin/user-management/notifications/list' },
        { title: '🕒 Crear Historial', route: '/admin/user-management/student-history/create' },
        { title: '📜 Ver Historial', route: '/admin/user-management/student-history/list' },
      ]
    },
    {
      title: '📚 Administración de Clases',
      options: [
        { title: '➕ Crear Grupo', route: '/admin/class-management/training-groups/create' },
        { title: '👥 Ver Grupos', route: '/admin/class-management/training-groups/list' },
        { title: '🧑‍🏫 Administrar Grupos', route: '/admin/class-management/training-groups/manage-students' },
        { title: '🗓️ Ver Horario', route: '/admin/class-management/training-groups/view-timetable' },
        { title: '📆 Ver Sesiones', route: '/admin/class-management/training-session/list' },
        { title: '📝 Registrar Asistencia', route: '/admin/class-management/assistance/create' },
        { title: '📋 Ver Asistencias', route: '/admin/class-management/assistance/list' },
        { title: '➕ Detalles de las Membresías', route: '/admin/class-management/memberships/details' },
        { title: '🏷️ Asignar Membresías', route: '/admin/class-management/memberships/list' },
      ]
    },
    {
      title: '💵 Finanzas',
      options: [
        { title: '🧾 Crear Factura', route: '/admin/finance/invoices/create' },
        { title: '📄 Ver Facturas', route: '/admin/finance/invoices/list' },
        { title: '📄 Nuevo Pago', route: '/admin/finance/payments/create' },
        { title: '🛒 Añadir Productos', route: '/admin/finance/products/create' },
        { title: '📦 Ver Productos', route: '/admin/finance/products/list' },
        { title: '💱 Añadir Tipo de IVA', route: '/admin/finance/ivatypes/create' },
      ]
    },
  ];

  return (
    <div className="dashboard">
      <h2>Panel de Control del Administrador</h2>
      {sections.map((section, index) => (
        <div key={index} className="dashboard-section">
          <h3>{section.title}</h3>
          <div className="card-grid">
            {section.options.map((opt, idx) => (
              <div key={idx} className="dashboard-card" onClick={() => navigate(opt.route)}>
                {opt.title}
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};

export default AdminDashboard;
