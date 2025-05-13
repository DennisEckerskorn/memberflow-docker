// src/components/layout/ContentArea.jsx
import React from 'react';
import '../styles/ContentArea.css';
import UserForm from '../forms/UserForm';
import StudentForm from '../forms/StudentForm';
import TeacherForm from '../forms/TeacherForm';
import { getRoleFromToken } from '../../utils/jwtHelper';

const ContentArea = ({ section, entity, action }) => {
  const role = getRoleFromToken();

  if (!section || !entity || !action) {
    return (
      <div className="content-area">
        <h2>{section || 'No section'} - {entity || 'No entity'} - {action || 'No action'}</h2>
        <p>Selecciona una opción del menú para comenzar.</p>
      </div>
    );
  }

  // Definir qué roles tienen acceso a qué entidades
  const permissions = {
    FULL_ACCESS: ['Users', 'Students', 'Teachers', 'Admin', 'Notifications', 'Assistance', 'TrainingGroups', 'TrainingSessions', 'Memberships', 'StudentHistories'],
    MANAGE_STUDENTS: ['Students', 'Assistance', 'TrainingGroups', 'TrainingSessions'],
    VIEW_OWN_DATA: ['Assistance', 'Timetable', 'History'],
  };

  // Validar si tiene acceso a la entidad actual
  const hasPermission = permissions[role]?.includes(entity);

  if (!hasPermission) {
    return (
      <div className="content-area">
        <h2>Acceso denegado</h2>
        <p>No tienes permiso para acceder a esta sección.</p>
      </div>
    );
  }

  // Sección de gestión de usuarios
  if (section === 'User Management') {
    if (action === 'create') {
      switch (entity) {
        case 'Users':
          return <div className="content-area"><UserForm /></div>;
        case 'Students':
          return <div className="content-area"><StudentForm /></div>;
        case 'Teachers':
          return <div className="content-area"><TeacherForm /></div>;
        case 'Admin':
          return <div className="content-area"><UserForm role="ROLE_ADMIN" /></div>;
        default:
          return <div className="content-area"><p>Entidad no reconocida.</p></div>;
      }
    }

    if (action === 'list') {
      return (
        <div className="content-area">
          <h2>Listado de {entity}</h2>
          <p>Aquí iría la lógica para listar {entity.toLowerCase()}.</p>
        </div>
      );
    }

    return (
      <div className="content-area">
        <h2>{action} {entity}</h2>
        <p>Esta acción aún no está implementada.</p>
      </div>
    );
  }

  return (
    <div className="content-area">
      <h2>{section}</h2>
      <p>Contenido en construcción para esta sección.</p>
    </div>
  );
};

export default ContentArea;
