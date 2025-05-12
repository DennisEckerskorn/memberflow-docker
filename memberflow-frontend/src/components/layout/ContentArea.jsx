// src/components/layout/ContentArea.jsx
import React from 'react';
import '../styles/ContentArea.css';
import UserForm from '../forms/UserForm';
import StudentForm from '../forms/StudentForm';
import TeacherForm from '../forms/TeacherForm';

const ContentArea = ({ section, entity, action }) => {
  // Si falta info, mostramos encabezado básico
  if (!section || !entity || !action) {
    return (
      <div className="content-area">
        <h2>{section || 'No section'} - {entity || 'No entity'} - {action || 'No action'}</h2>
        <p>Selecciona una opción del menú para comenzar.</p>
      </div>
    );
  }

  // Sección de User Management
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

    // Puedes continuar con update, delete, findById, etc.
    return (
      <div className="content-area">
        <h2>{action} {entity}</h2>
        <p>Esta acción aún no está implementada.</p>
      </div>
    );
  }

  // Si no se reconoce la sección
  return (
    <div className="content-area">
      <h2>{section}</h2>
      <p>Contenido en construcción para esta sección.</p>
    </div>
  );
};

export default ContentArea;
